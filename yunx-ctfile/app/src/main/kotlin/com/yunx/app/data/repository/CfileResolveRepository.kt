package com.yunx.app.data.repository

import com.yunx.app.data.network.CfileApi
import com.yunx.app.data.network.ShareLinkParser
import com.yunx.app.data.network.model.DownloadLink
import com.yunx.app.data.network.model.ShareFile
import com.yunx.app.data.network.model.ShareSession

/**
 * 城通网盘（CTFile）分享解析仓库。
 *
 * 城通是单文件直链解析服务（无文件夹、无转存），与 YunX 的 [ShareResolveRepository] 适配如下：
 * - [createSession]：从链接提取文件ID（/f/ 后）与提取码，立即解析直链并缓存；
 * - [listFiles]：返回一个代表该单文件本身的 [ShareFile]（城通无目录结构）；
 * - [getShareDownloadLink]：返回 createSession 时已解析的直链；
 * - [transferFile] / [ensureTempDir]：城通不支持转存，抛 UnsupportedOperationException。
 *
 * @param tokenProvider 当前城通 tempToken（ResolveViewModel 从 CfileAccountRepository 提供）
 */
class CfileResolveRepository(
    private val api: CfileApi,
    private val tokenProvider: suspend () -> String?
) : ShareResolveRepository {

    /** 缓存最近一次解析出的直链（createSession 时填充，供 listFiles / getShareDownloadLink 使用） */
    private var cachedLink: DownloadLink? = null

    /** 城通解析文件ID：从链接 /f/ 后提取（如 5062648-976790602-05189e） */
    private fun extractFileId(link: String): String? {
        val m = Regex(CfileConstantsPattern).find(link) ?: return null
        return m.groupValues.getOrNull(1)
    }

    override suspend fun createSession(link: String, pwd: String?, cookie: String): Result<ShareSession> {
        val token = tokenProvider().orEmpty().ifBlank { cookie.ifBlank { "" } }
        if (token.isBlank()) {
            return Result.failure(IllegalStateException("请先在「设置」中配置城通 tempToken"))
        }
        val fileId = extractFileId(link)
            ?: return Result.failure(IllegalArgumentException("无法识别城通分享链接"))
        // 提取码：用户手输 > 链接自带
        val passcode = pwd?.takeIf { it.isNotBlank() }
            ?: Regex(CfileConstantsPattern).find(link)?.groupValues?.getOrNull(2).orEmpty()
        return runCatching {
            val result = api.resolve(fileId, passcode, token).getOrElse { throw it }
            cachedLink = result
            ShareSession(shareId = fileId, stoken = "", title = result.filename)
        }
    }

    override suspend fun listFiles(session: ShareSession, dirFid: String, cookie: String): Result<List<ShareFile>> =
        runCatching {
            val link = cachedLink ?: return@runCatching emptyList()
            listOf(
                ShareFile(
                    fid = link.fid,
                    fname = link.filename,
                    fsize = link.size,
                    isdir = false,
                    pdirFid = "0",
                    fidToken = ""
                )
            )
        }

    /** 城通无需转存 */
    override suspend fun ensureTempDir(cookie: String): Result<String> =
        Result.failure(UnsupportedOperationException("城通不支持转存"))

    /** 城通不支持转存 */
    override suspend fun transferFile(
        session: ShareSession,
        file: ShareFile,
        toDirFid: String,
        cookie: String
    ): Result<String> = Result.failure(UnsupportedOperationException("城通不支持转存"))

    override suspend fun getDownloadLink(fid: String, cookie: String): Result<DownloadLink> =
        Result.failure(UnsupportedOperationException("请使用城通分享解析"))

    override suspend fun getShareDownloadLink(
        session: ShareSession,
        file: ShareFile,
        cookie: String
    ): Result<DownloadLink> = runCatching {
        cachedLink ?: throw IllegalStateException("城通解析结果已失效，请重新解析")
    }

    companion object {
        private const val CfileConstantsPattern = "https?://[^/\\s]+\\.ctfile\\.com/f/(\\d+-\\d+-\\w+)(?:\\?p=([A-Za-z0-9]+))?"
    }
}