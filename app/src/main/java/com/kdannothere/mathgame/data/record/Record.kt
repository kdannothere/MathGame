package com.kdannothere.mathgame.data.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kdannothere.mathgame.data.GameDatabase

@Entity(tableName = GameDatabase.tableRecords)
data class Record(
    @PrimaryKey val timeStamp: String,
    @ColumnInfo val date: String,
    @ColumnInfo val topic: String,
    @ColumnInfo val level: String,
    @ColumnInfo val correct: String,
    @ColumnInfo val mistakes: String,
    @ColumnInfo val skipped: String,
)