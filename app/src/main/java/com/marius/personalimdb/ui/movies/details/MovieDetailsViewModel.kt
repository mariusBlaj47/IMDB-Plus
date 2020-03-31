package com.marius.personalimdb.ui.movies.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.model.*
import com.marius.personalimdb.data.repository.ImdbRepository
import com.marius.personalimdb.data.repository.MovieRepository

class MovieDetailsViewModel : ViewModel() {
    val movie = MutableLiveData<Movie>()
    val rating = MutableLiveData<ImdbRating>(ImdbRating("-", "-"))
    val releaseDate = MutableLiveData<String>()
    val duration = MutableLiveData<String>()
    val genres = MutableLiveData<String>("")
    val similarMovies = MutableLiveData(mutableListOf<Movie>())
    val recommendedMovies = MutableLiveData(mutableListOf<Movie>())
    val actorList = MutableLiveData(mutableListOf<Actor>())
    val trailer = MutableLiveData<Video>()
    val posters = MutableLiveData(mutableListOf<Media>())
    val startImdb = MutableLiveData<Unit>()
    val isInWatchlist = MutableLiveData<Boolean>(false)

    fun getData(movieId: Int) {
        getDetails(movieId)
        checkWatchlist(movieId)
        getTrailer(movieId)
        getCast(movieId)
        getSimilar(movieId)
        getRecommended(movieId)
    }

    fun changeWatchListStatus() {
        isInWatchlist.value?.let { status ->
            Account.details.sessionId?.let { session ->
                movie.value?.id?.let { movieId ->
                    MovieRepository.changeWatchlistStatus(movieId, status, session) {
                        isInWatchlist.value = !status
                    }
                }
            }
        }
    }

    private fun checkWatchlist(movieId: Int) {
        Account.details.sessionId?.let { session ->
            MovieRepository.getWatchlistStatus(movieId, session) { status ->
                isInWatchlist.value = status
            }
        }
    }

    private fun getTrailer(movieId: Int) {
        MovieRepository.getMovieTrailer(movieId) { video ->
            trailer.value = video
            getPhotos(movieId)
        }
    }

    private fun getPhotos(movieId: Int) {
        MovieRepository.getBackdrops(movieId) { backdrops ->
            backdrops.filter {
                it.ratio > 1.5
            }
            posters.value = backdrops.take(6).toMutableList()
        }
    }

    private fun getCast(movieId: Int) {
        MovieRepository.getMovieCast(movieId) {
            actorList.value = it.toMutableList()
        }
    }

    private fun getRecommended(movieId: Int) {
        MovieRepository.getRecommendedMovies(movieId) { recommended ->
            recommendedMovies.value?.clear()
            recommendedMovies.value = recommended.toMutableList()
        }
    }

    private fun getSimilar(movieId: Int) {
        MovieRepository.getSimilarMovies(movieId) { similar ->
            similarMovies.value?.clear()
            similarMovies.value = similar.toMutableList()
        }
    }

    private fun getDetails(movieId: Int) {
        MovieRepository.getMovieDetails(movieId) { details ->
            movie.value = details.movie
            genres.value = details.genres
            getRating(details.imdbId)
            releaseDate.value = details.releaseDate
            duration.value = details.duration
        }
    }

    private fun getRating(imdbId: String) {
        ImdbRepository.getRating(imdbId) {
            rating.value = it
        }
    }

    /**
     * Trigger the opening of the IMDB link
     */
    fun openImdb() {
        startImdb.value = Unit
    }

}