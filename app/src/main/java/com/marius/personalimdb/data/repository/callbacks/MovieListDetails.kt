package com.marius.personalimdb.data.repository.callbacks

import com.marius.personalimdb.data.model.Movie

data class MovieListDetails(val movieList: List<Movie>, val totalPages: Int)