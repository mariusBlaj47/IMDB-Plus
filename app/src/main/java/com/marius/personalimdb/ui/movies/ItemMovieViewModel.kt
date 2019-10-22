package com.marius.personalimdb.ui.movies

import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.helper.OpensMovieDetails
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class ItemMovieViewModel(movie: Movie, private val callback: OpensMovieDetails) {
    val id: Int = movie.id
    val posterDimension = 200
    val poster: String = movie.poster.toString()
    val title: String = movie.title
    val progress: Int = ((movie.rating * 10).toInt())
    val description = movie.description
    val releaseDate: String
    val releaseYear: String
    val progressText: SpannableString = upPercent("$progress%")

    init {
        val release = LocalDate.parse(movie.releaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        releaseDate = "${release.dayOfMonth} ${release.month} ${release.year}"
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
        callback.onMovieClicked(id)
    }

}