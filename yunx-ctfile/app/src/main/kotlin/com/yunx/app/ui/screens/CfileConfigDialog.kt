package com.yunx.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 城通网盘凭证配置弹窗：粘贴 tempToken。
 *
 * tempToken 获取方式：浏览器登录城通后，F12 → Network → 任意请求头的 cookie 里 `tempToken=` 后面的
 * 约 22 位字符串。token 与登录设备绑定，更换设备/重新登录后需重新更新。
 */
@Composable
fun CfileConfigDialog(
    initialToken: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var token by remember { mutableStateOf(initialToken) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("城通网盘 tempToken") },
        text = {
            Column {
                Text("将城通网盘浏览器登录后取得的 tempToken（cookie 里，约 22 位）粘贴到下方：")
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = token,
                    onValueChange = { token = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    minLines = 2,
                    maxLines = 3,
                    label = { Text("tempToken") },
                    placeholder = { Text("例如 88mxm7eue7y73j6y2h33f") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(token.trim()) }) { Text("保存") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
}