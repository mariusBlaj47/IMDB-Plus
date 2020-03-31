package com.marius.personalimdb.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TvShowDate(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "last_air_date") var lastAirDate: String,
    @ColumnInfo(name = "follow") var followed: Boolean
)