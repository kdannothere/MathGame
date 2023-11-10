package com.kdannothere.mathgame.data.record

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kdannothere.mathgame.data.AppDatabase

@Dao
interface RecordDao {

    @Upsert
    suspend fun upsertRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query("SELECT * FROM ${AppDatabase.tableRecords}")
    suspend fun getRecords(): List<Record>
}
