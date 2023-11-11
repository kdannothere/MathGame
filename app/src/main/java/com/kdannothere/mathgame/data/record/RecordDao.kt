package com.kdannothere.mathgame.data.record

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RecordDao {

    @Upsert
    suspend fun upsertRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query("SELECT * FROM Records WHERE date BETWEEN :from AND :to")
    suspend fun getRecordsBetweenTwoDates(from: String, to: String): List<Record>
}
