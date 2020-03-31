package com.marius.personalimdb.data.repository.callbacks

import com.marius.personalimdb.data.model.TvShow

data class TvShowDetails(
    val tvShow: TvShow,
    val imdbId: String,
    val releaseYear: String,
    val firstEpisode: String,
    val seasons: String,
    val lastEpisode: String,
    val episodes: String,
    val wastedTime: String,
    val episodeDuration: String,
    val genres: String
)