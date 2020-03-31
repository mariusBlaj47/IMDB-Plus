package com.marius.personalimdb.ui.tvShows.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.model.*
import com.marius.personalimdb.data.repository.ImdbRepository
import com.marius.personalimdb.data.repository.TvShowRepository
import com.marius.personalimdb.database.WatchlistDatabase
import kotlin.concurrent.thread

class TvShowDetailsViewModel : ViewModel() {
    val tvShow = MutableLiveData<TvShow>()
    val releaseYear = MutableLiveData<String>()
    val firstEpisode = MutableLiveData<String>("-")
    val lastEpisode = MutableLiveData<String>("-")
    val seasons2 = MutableLiveData<String>("-")
    val episodes = MutableLiveData<String>("-")
    val wastedTime = MutableLiveData<String>("-")
    val seasons = MutableLiveData<String>()
    val episodeDuration = MutableLiveData<String>("-")
    val genres = MutableLiveData<String>("")
    val trailer = MutableLiveData<Video>()
    val posters = MutableLiveData(mutableListOf<Media>())
    val actorList = MutableLiveData(mutableListOf<Actor>())
    val similarTvShows = MutableLiveData(mutableListOf<TvShow>())
    val recommendedTvShows = MutableLiveData(mutableListOf<TvShow>())
    val rating = MutableLiveData<ImdbRating>(ImdbRating("-", "-"))
    val startImdb = MutableLiveData<Unit>()
    val isInWatchlist = MutableLiveData(false)
    val isFollowing = MutableLiveData(false)
    val errorToast = MutableLiveData<String>()

    lateinit var db: WatchlistDatabase


    fun getData(tvShowId: Int) {
        getDetails(tvShowId)
        getFollowStatus(tvShowId)
        checkWatchlist(tvShowId)
        getSimilar(tvShowId)
        getRecommended(tvShowId)
        getTrailer(tvShowId)
        getCast(tvShowId)
    }

    private fun getFollowStatus(tvShowId: Int) {
        thread {
            val currentShow = db.tvShowDao().getTvShowDate(tvShowId)
            if (currentShow != null)
                isFollowing.postValue(currentShow.followed)
            else isFollowing.postValue(false)
        }
    }

    fun changeFollow() {
        thread {
            tvShow.value?.id?.let {
                val currentFollow = isFollowing.value
                val currentDbEntry = db.tvShowDao().getTvShowDate(it)
                if (currentDbEntry != null) {
                    currentDbEntry.followed = !currentFollow!!
                    isFollowing.postValue(!currentFollow)
                    db.tvShowDao().insert(currentDbEntry)
                } else {
                    errorToast.postValue("You must have this show in your watch list to follow")
                }
            }
        }
    }

    fun changeWatchListStatus() {
        isInWatchlist.value?.let { status ->
            Account.details.sessionId?.let { session ->
                tvShow.value?.id?.let { tvShowId ->
                    TvShowRepository.changeWatchlistStatus(tvShowId, status, session) {
                        isInWatchlist.value = !status
                        changeWatchListDatabaseFollow(tvShowId, !status)
                    }
                }
            }

        }
    }

    private fun changeWatchListDatabaseFollow(tvShowId: Int, inWatchlist: Boolean) {
        thread {
            if (inWatchlist) {
                tvShow.value?.lastAirDate?.let { lastDate ->
                    db.tvShowDao().insert(TvShowDate(tvShowId, lastDate, true))
                }
            } else {
                val currentDbEntry = db.tvShowDao().getTvShowDate(tvShowId)
                db.tvShowDao().remove(currentDbEntry)
            }
            getFollowStatus(tvShowId)
        }
    }

    private fun checkWatchlist(tvShowId: Int) {
        Account.details.sessionId?.let { session ->
            TvShowRepository.getWatchlistStatus(tvShowId, session) { status ->
                isInWatchlist.value = status
            }
        }
    }

    fun updateLastAirDate() {
        thread {
            tvShow.value?.id?.let { id ->
                tvShow.value?.lastAirDate?.let { lastAirDate ->
                    val dbShow = db.tvShowDao().getTvShowDate(id)
                    dbShow.lastAirDate = lastAirDate
                    db.tvShowDao().insert(dbShow)
                }
            }
        }
    }

    private fun getDetails(tvShowId: Int) {
        TvShowRepository.getTvShowDetails(tvShowId) { details ->
            tvShow.value = details.tvShow
            getRating(details.imdbId)
            releaseYear.value = details.releaseYear
            firstEpisode.value = details.firstEpisode
            lastEpisode.value = details.lastEpisode
            episodes.value = details.episodes
            wastedTime.value = details.wastedTime
            episodeDuration.value = details.episodeDuration
            genres.value = details.genres
            seasons2.value = details.seasons
            seasons.value = "${details.seasons} ● "
        }
    }

    private fun getRating(imdbId: String) {
        ImdbRepository.getRating(imdbId) {
            rating.value = it
        }
    }

    private fun getCast(tvShowId: Int) {
        TvShowRepository.getTvShowCast(tvShowId) {
            actorList.value = it.toMutableList()
        }
    }

    private fun getTrailer(tvShowId: Int) {
        TvShowRepository.getTvShowTrailer(tvShowId) { video ->
            video?.let {
                trailer.value = video
            }
            getPhotos(tvShowId)
        }
    }

    private fun getPhotos(tvShowId: Int) {
        TvShowRepository.getBackdrops(tvShowId) { photos ->
            photos.filter {
                it.ratio > 1.5
            }
            posters.value = photos.take(6).toMutableList()
        }
    }

    private fun getSimilar(tvShowId: Int) {
        TvShowRepository.getSimilarTvShows(tvShowId) {
            similarTvShows.value = it.toMutableList()
        }
    }

    private fun getRecommended(tvShowId: Int) {
        TvShowRepository.getRecommendedTvShows(tvShowId) {
            recommendedTvShows.value = it.toMutableList()
        }
    }

    fun openImdb() {
        startImdb.value = Unit
    }
}