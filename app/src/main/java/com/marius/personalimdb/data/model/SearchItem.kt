package com.marius.personalimdb.data.model

import com.google.gson.annotations.SerializedName

data class SearchItem(
    @SerializedName(value = "media_type") val type: String,
    @SerializedName(value = "id") val id: Int,
    @SerializedName(value = "name") val name: String,
    @SerializedName(value = "character") var role: String,
    @SerializedName(value = "profile_path") val profilePhoto: String?,
    @SerializedName(value = "biography") val biography: String?,
    @SerializedName(value = "gender") val gender: Int,
    @SerializedName(value = "known_for") var credits: MutableList<Credit>?,
    @SerializedName(value = "title") val title: String,
    @SerializedName(value = "release_date") val releaseDate: String?,
    @SerializedName(value = "vote_average") val rating: Float,
    @SerializedName(value = "overview") val description: String?,
    @SerializedName(value = "poster_path") val poster: String?,
    @SerializedName(value = "runtime") var duration: Int,
    @SerializedName(value = "genres") val genres: List<Genre>,
    @SerializedName(value = "popularity") val popularity: Float,
    @SerializedName(value = "imdb_id") val imdbId: String?,
    @SerializedName(value = "first_air_date") val firstAirDate: String?,
    @SerializedName(value = "number_of_seasons") val seasons: Int?,
    @SerializedName(value = "number_of_episodes") val episodes: Int?,
    @SerializedName(value = "episode_run_time") val runtime: List<Int>?,
    @SerializedName(value = "last_air_date") val lastAirDate: String?
) {
    fun toMovie(): Movie {
        return Movie(
            this.id,
            this.title,
            this.releaseDate!!,
            this.rating,
            1,
            this.description!!,
            this.poster,
            this.duration,
            this.genres,
            this.popularity,
            null
        )
    }

    fun toTvShow(): TvShow {
        return TvShow(
            this.id,
            this.name,
            this.firstAirDate!!,
            this.rating,
            1,
            this.description!!,
            this.poster,
            this.seasons,
            this.episodes,
            this.runtime,
            this.genres,
            this.popularity,
            this.lastAirDate,
            null
        )
    }

    fun toActor(): Actor {
        return Actor(
            this.id,
            this.name,
            this.role,
            this.profilePhoto,
            this.biography,
            this.gender,
            this.credits
        )
    }
}