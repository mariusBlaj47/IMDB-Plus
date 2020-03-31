package com.marius.personalimdb.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryItem(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "release") val releaseDate: String,
    @ColumnInfo(name = "description") val summary: String,
    @ColumnInfo(name = "score") val rating: Float,
    @ColumnInfo(name = "poster") val posterPath: String?,
    @ColumnInfo(name = "modified_at") var lastDate: Long,
    @ColumnInfo(name = "type") val type: String
) {
    companion object {
        fun fromMovie(movie: Movie): HistoryItem {
            return HistoryItem(
                movie.id,
                movie.title,
                movie.releaseDate,
                movie.description,
                movie.rating,
                movie.poster, 1,
                "Movie"
            )
        }

        fun fromTvShow(tvShow: TvShow): HistoryItem {
            return HistoryItem(
                tvShow.id,
                tvShow.name,
                tvShow.firstAirDate,
                tvShow.description,
                tvShow.rating,
                tvShow.poster, 1,
                "TvShow"
            )
        }

        fun fromActor(actor: Actor): HistoryItem {
            return HistoryItem(
                actor.id,
                actor.name,
                "", "", 1f,
                actor.profilePhoto, 1,
                "Actor"
            )
        }
    }

    fun toMovie(): Movie {
        return Movie(
            this.id,
            this.name,
            this.releaseDate,
            this.rating,
            1,
            this.summary,
            this.posterPath,
            1,
            null,
            1.0f,
            null
        )
    }

    fun toTvShow(): TvShow {
        return TvShow(
            this.id,
            this.name,
            this.releaseDate,
            this.rating,
            1,
            this.summary,
            this.posterPath,
            1,
            1,
            null,
            null,
            1f,
            null,
            null
        )
    }

    fun toActor(): Actor {
        return Actor(
            this.id,
            this.name,
            null,
            this.posterPath,
            null,
            1,
            null
        )
    }
}