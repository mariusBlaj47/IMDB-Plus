package com.marius.personalimdb.data.repository.callbacks

import com.marius.personalimdb.data.model.SearchContent

data class SearchDetails(val searchList: MutableList<SearchContent>, val totalPages: Int)