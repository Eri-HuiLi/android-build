package com.yunx.app.data.network

/**
 * 城通网盘（CTFile）常量。
 *
 * 城通网盘是"网盘下载站"型服务，分享链接为单文件直链解析，无文件夹结构：
 * - 分享链接：https://{子域}.ctfile.com/f/{文件ID}-{文件序号}-{哈希}?p={提取码}
 * - 解析 API：GET webapi.ctfile.com/getfile.php 返回 JSON，code==200 时 file.vip_dx_url 为直链
 *
 * 凭证：tempToken（约 22 位字符串）。需登录城通后从浏览器 Network 中取得（cookie 值 tempToken=...）。
 * 城通按会员区分：非会员单任务下载且限速约 80KB/s；解析直链有效约 12 小时。
 */
object CfileConstants {

    /** 解析 API 主机（webapi.ctfile.com） */
    const val WEBAPI_HOST = "https://webapi.ctfile.com"

    /** 解析共享文件直链（GET，参数见 [CfileApi.resolve]）：f=文件ID, passcode=提取码, token=tempToken */
    const val RESOLVE_URL = "$WEBAPI_HOST/getfile.php"

    /** 解析链接正则：https://<子域>.ctfile.com/f/<文件ID>-<序号>-<哈希>?p=<提取码> */
    const val SHARE_URL_PATTERN = """https?://[^/\s]+\.ctfile\.com/f/(\d+-\d+-\w+)(?:\?p=([A-Za-z0-9]+))?"""

    /** 桌面浏览器 UA（请求 getfile.php 用） */
    const val WEB_UA =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36"

    /** 城通解析需额外携带的 Referer（对齐 web 客户端行为） */
    const val REFERER = "https://www.ctfile.com/"
}