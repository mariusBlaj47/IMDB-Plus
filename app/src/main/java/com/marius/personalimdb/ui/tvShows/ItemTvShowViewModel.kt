package com.marius.personalimdb.ui.tvShows

import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.helper.OpensTvShowDetails
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class ItemTvShowViewModel(tvShow: TvShow, private val callback: OpensTvShowDetails) {
    val id: Int = tvShow.id
    val posterDimension = 200
    val poster: String = tvShow.poster.toString()
    val title: String = tvShow.name
    val progress: Int = ((tvShow.rating * 10).toInt())
    val description = tvShow.description
    val releaseYear: String
    val progressText: SpannableString = upPercent("$progress%")

    init {
        val release =
            LocalDate.parse(tvShow.firstAirDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        releaseYear = release.year.toString()
    }

    private fun upPercent(str: String): SpannableString {
        return SpannableString(str).apply {
            setSpan(SuperscriptSpan(), str.length - 1, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(
                RelativeSizeSpan(0.4f),
                str.length - 1,
                str.length,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
    }

    fun onClick() {
        callback.onTvShowClicked(id)
    }
}