package com.yunx.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 城通网盘凭证（tempToken 落库）。
 *
 * 城通不提供账号密码登录 API，解析依赖浏览器登录后取得的 tempToken（cookie 里的 22 位字符串）。
 * 因 token 与登录设备绑定，更换设备/重新登录后需在「设置」里重新粘贴更新。
 */
@Entity(tableName = "cfile_account")
data class CfileAccountEntity(
    @PrimaryKey
    val id: String = "cfile",
    /** 城通 tempToken（浏览器登录后 Network 中取得） */
    val token: String = "",
    /** 展示用昵称/账号（可空） */
    val nickname: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)