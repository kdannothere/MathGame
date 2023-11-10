package com.kdan.tryyourluckwheel.data.record

import com.kdannothere.mathgame.data.record.Record
import com.kdannothere.mathgame.data.record.RecordDao

class RecordRepository (private val dao: RecordDao) {

    suspend fun upsertRecord(record: Record) = dao.upsertRecord(record)

    suspend fun deleteRecord(record: Record) = dao.deleteRecord(record)

    suspend fun getRecords(): List<Record> = dao.getRecords()
}