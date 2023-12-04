package com.kdannothere.mathgame.data.record

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.DeleteTable
import androidx.room.Query
import androidx.room.Upsert
import com.kdannothere.mathgame.data.GameDatabase

@Dao
interface RecordDao {

    @Upsert
    suspend fun upsertRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query("DELETE FROM Records")
    suspend fun deleteAllRecords()

    @Query("SELECT * FROM Records WHERE date = :date")
    suspend fun getRecordsForDate(date: String): List<Record>
}
