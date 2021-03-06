package com.marius.personalimdb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marius.personalimdb.data.model.HistoryItem
import com.marius.personalimdb.data.model.TvShowDate

@Database(entities = [HistoryItem::class], version = 47,exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDAO
}