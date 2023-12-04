package com.kdannothere.mathgame.data.record

class RecordRepository(private val dao: RecordDao) {

    suspend fun upsertRecord(record: Record) = dao.upsertRecord(record)

    suspend fun deleteAllRecords() = dao.deleteAllRecords()

    suspend fun getRecordsFromDate(date: String): List<Record> =
        dao.getRecordsForDate(date)
}