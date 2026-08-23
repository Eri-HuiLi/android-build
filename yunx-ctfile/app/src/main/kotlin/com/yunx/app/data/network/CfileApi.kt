package com.yunx.app.data.network

import com.yunx.app.data.network.model.DownloadLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.ThreadLocalRandom

/**
 * 城通网盘（CTFile）API 封装（OkHttp）。
 *
 * 城通是单文件直链解析服务，不涉及账号登录 / 文件管理 / 转存：
 * - [resolve]：GET webapi.ctfile.com/getfile.php，携带文件ID、提取码、token（tempToken），返回直链；
 *   code==200 时 file.vip_dx_url 为下载直链，否则按 code 判断失败原因（非会员/密码错误等）。
 */
class CfileApi(
    private val clientProvider: () -> OkHttpClient = { HttpClients.apiClient() }
) {
    private val client get() = clientProvider()

    /**
     * 解析城通共享文件直链。
     * @param file 分享链接 /f/ 后的文件ID（如 5062648-976790602-05189e）
     * @param passcode 提取码（链接 ?p= 后；无密码可传空串）
     * @param token 城通 tempToken（约 22 位）
     * @return 解析成功返回 [DownloadLink]；失败返回失败原因文本
     */
    suspend fun resolve(file: String, passcode: String, token: String): Result<DownloadLink> =
        withContext(Dispatchers.IO) {
            runCatching {
                val url = CfileConstants.RESOLVE_URL.toHttpUrl().newBuilder()
                    .addQueryParameter("path", "f")
                    .addQueryParameter("f", file)
                    .addQueryParameter("passcode", passcode)
                    .addQueryParameter("token", token)
                    .addQueryParameter("r", ThreadLocalRandom.current().nextDouble().toString())
                    .addQueryParameter("ref", "")
                    .build()
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", CfileConstants.WEB_UA)
                    .header("Referer", CfileConstants.REFERER)
                    .get()
                    .build()
                client.newCall(request).execute().use { resp ->
                    val body = resp.body?.string().orEmpty()
                    val json = JSONObject(body)
                    val code = json.optInt("code", -1)
                    if (code == 200) {
                        val fileObj = json.optJSONObject("file")
                            ?: throw IllegalStateException("城通响应缺少 file 字段")
                        val directUrl = fileObj.optString("vip_dx_url").ifBlank {
                            fileObj.optString("dx_url")
                        }
                        if (directUrl.isBlank()) throw IllegalStateException("城通解析未返回直链")
                        val fname = fileObj.optString("file_name").ifBlank { fileObj.optString("title") }
                        val fsize = fileObj.optLong("file_size", 0L)
                        DownloadLink(
                            fid = file,
                            filename = fname.ifBlank { file },
                            downloadUrl = directUrl,
                            size = fsize
                        )
                    } else {
                        // 常见错误码：400 密码错误 / 402 未授权（token 失效）
                        val msg = when (code) {
                            402 -> "城通 token 无效或已失效，请重新获取 tempToken"
                            400 -> "城通提取码错误"
                            401 -> "该文件可能已失效或需城通会员"
                            else -> "城通解析失败（code=$code）"
                        }
                        throw IllegalStateException(msg)
                    }
                }
            }
        }
}