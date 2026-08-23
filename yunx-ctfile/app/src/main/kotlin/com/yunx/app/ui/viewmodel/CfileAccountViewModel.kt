package com.yunx.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yunx.app.data.db.CfileAccountEntity
import com.yunx.app.data.repository.CfileAccountRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 城通网盘凭证 ViewModel：保存/清除 tempToken，暴露凭证态供「设置」页与解析页共享。
 */
class CfileAccountViewModel(
    private val repository: CfileAccountRepository
) : ViewModel() {

    val cfileAccount: StateFlow<CfileAccountEntity?> = repository.observeAccount()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    var saveMessage by mutableStateOf<String?>(null)
        private set

    fun consumeSaveMessage() {
        saveMessage = null
    }

    /** 保存 tempToken（城通凭证） */
    fun saveToken(token: String) {
        viewModelScope.launch {
            saveMessage = null
            if (token.isBlank()) {
                saveMessage = "城通 tempToken 不能为空"
                return@launch
            }
            repository.saveToken(token)
            saveMessage = "城通 token 已保存"
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            saveMessage = "已清除城通凭证"
        }
    }

    class Factory(
        private val repository: CfileAccountRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(CfileAccountViewModel::class.java))
            return CfileAccountViewModel(repository) as T
        }
    }
}