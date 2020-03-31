package com.marius.personalimdb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.marius.personalimdb.data.model.HistoryItem
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.model.TvShowDate

@Database(entities = [TvShowDate::class], version = 3,exportSchema = false)
abstract class WatchlistDatabase : RoomDatabase() {
    abstract fun tvShowDao(): TvShowDateDAO
}