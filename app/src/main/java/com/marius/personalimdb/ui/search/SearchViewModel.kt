package com.marius.personalimdb.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.model.*
import com.marius.personalimdb.data.repository.SearchRepository
import com.marius.personalimdb.database.HistoryDatabase
import kotlin.concurrent.thread

class SearchViewModel : ViewModel() {
    val queryString = MutableLiveData<String>()
    val resetTrigger = MutableLiveData<Unit>()
    val searchedItems = MutableLiveData(mutableListOf<SearchContent>())
    var isLoading = false
    var lastLoadedPage = 0
    var totalPages = 1
    private lateinit var db: HistoryDatabase

    fun saveHistoryItem(id: Int, type: String) {
        val item = getItemById(id)
        thread {
            item?.let {
                when (type) {
                    "Movie" -> db.historyDao()
                        .insertWithTimestamp(HistoryItem.fromMovie(item as Movie))
                    "TvShow" -> db.historyDao()
                        .insertWithTimestamp(HistoryItem.fromTvShow(item as TvShow))
                    "Actor" -> db.historyDao()
                        .insertWithTimestamp(HistoryItem.fromActor(item as Actor))
                }
                val dbHistory = db.historyDao().getAll()
                if (dbHistory.size > 20) {
                    db.historyDao().delete(dbHistory.last())
                }
            }
        }
    }

    private fun getItemById(id: Int): SearchContent? {
        searchedItems.value?.forEach {
            if (it is Movie) {
                if (it.id == id) {
                    return it
                }
            } else if (it is TvShow) {
                if (it.id == id) {
                    return it
                }
            } else if (it is Actor) {
                if (it.id == id) {
                    return it
                }
            }
        }
        return null
    }

    fun setHistory(db: HistoryDatabase) {
        this.db = db
        thread {
            val history = db.historyDao().getAll()
            val historyList = mutableListOf<SearchContent>()
            history.forEach {
                when (it.type) {
                    "Movie" -> historyList.add(it.toMovie())
                    "TvShow" -> historyList.add(it.toTvShow())
                    "Actor" -> historyList.add(it.toActor())
                }
            }
            searchedItems.postValue(historyList)
        }
    }

    fun initSearch() {
        searchContent()
    }

    fun extendSearchPage(page: Int) {
        if (page > totalPages) {
            return
        }
        searchContent(page)
    }


    private fun searchContent(page: Int = 1) {
        if (page == 1)
            lastLoadedPage = 0
        val query = queryString.value
        if (!query.isNullOrBlank()) {
            isLoading = true
            SearchRepository.searchByQuery(query, page) { details ->
                lastLoadedPage++
                totalPages = details.totalPages
                searchedItems.value = details.searchList
                isLoading = false
            }
        }
    }

}