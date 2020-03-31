package com.marius.personalimdb.data.repository.callbacks
import com.marius.personalimdb.data.model.Movie

data class MovieDetails(val movie:Movie,val imdbId:String,val releaseDate:String,val duration: String,val genres:String)