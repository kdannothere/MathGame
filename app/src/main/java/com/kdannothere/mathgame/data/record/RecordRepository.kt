package com.kdannothere.mathgame.data.record

class RecordRepository(private val dao: RecordDao) {

    suspend fun upsertRecord(record: Record) = dao.upsertRecord(record)

    suspend fun deleteRecord(record: Record) = dao.deleteRecord(record)

    suspend fun getRecordsBetweenTwoDates(from: String, to: String): List<Record> =
        dao.getRecordsBetweenTwoDates(from, to)
}