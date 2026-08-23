package com.yunx.app.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CfileAccountDao {

    @Query("SELECT * FROM cfile_account WHERE id='cfile'")
    fun observeAccount(): Flow<CfileAccountEntity?>

    @Query("SELECT * FROM cfile_account WHERE id='cfile'")
    suspend fun getAccount(): CfileAccountEntity?

    @Upsert
    suspend fun upsert(account: CfileAccountEntity)

    @Query("DELETE FROM cfile_account WHERE id='cfile'")
    suspend fun clear()
}