package com.kdannothere.mathgame.data.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kdannothere.mathgame.data.AppDatabase

@Entity(tableName = AppDatabase.tableRecords)
data class Record(
    @PrimaryKey val time: String,
    @ColumnInfo val topic: String,
    @ColumnInfo val level: String,
    @ColumnInfo val correct: String,
    @ColumnInfo val mistakes: String,
    @ColumnInfo val skipped: String,
)