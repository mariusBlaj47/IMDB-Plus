package com.marius.personalimdb.database

import androidx.room.*
import com.marius.personalimdb.data.model.TvShowDate

@Dao
interface TvShowDateDAO {
    @Query("SELECT * FROM tvshowdate")
    fun getAll(): List<TvShowDate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tvShowDate: TvShowDate)

    @Query("SELECT * FROM tvshowdate WHERE id = :id")
    fun getTvShowDate(id: Int): TvShowDate

    @Delete
    fun remove(tvShowDate: TvShowDate)

    @Query("DELETE FROM tvshowdate")
    fun nukeTable()
}