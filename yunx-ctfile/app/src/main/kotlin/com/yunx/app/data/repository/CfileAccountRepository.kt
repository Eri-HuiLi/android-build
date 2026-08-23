package com.yunx.app.data.repository

import com.yunx.app.data.db.CfileAccountDao
import com.yunx.app.data.db.CfileAccountEntity
import com.yunx.app.data.network.CfileApi
import kotlinx.coroutines.flow.Flow

/**
 * 城通网盘凭证仓库：保存 / 校验 / 清除 tempToken。
 *
 * 城通凭证 = 浏览器登录后取得的 tempToken（22 位字符串），非账号密码。
 * token 与登录设备绑定：更换设备 / 重新登录后需更新。
 */
class CfileAccountRepository(
    private val dao: CfileAccountDao,
    private val api: CfileApi
) {

    fun observeAccount(): Flow<CfileAccountEntity?> = dao.observeAccount()

    suspend fun getAccount(): CfileAccountEntity? = dao.getAccount()

    suspend fun getToken(): String? = dao.getAccount()?.token?.takeIf { it.isNotBlank() }

    /** 保存/更新 tempToken 凭证 */
    suspend fun saveToken(token: String, nickname: String = "") {
        if (token.isBlank()) return
        dao.upsert(
            CfileAccountEntity(
                id = "cfile",
                token = token.trim(),
                nickname = nickname
            )
        )
    }

    suspend fun logout() {
        dao.clear()
    }
}