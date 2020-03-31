package com.marius.personalimdb.service

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.marius.personalimdb.R
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.repository.AccountRepository
import com.marius.personalimdb.data.repository.TvShowRepository
import com.marius.personalimdb.database.WatchlistDatabase
import com.marius.personalimdb.data.model.TvShowDate
import com.marius.personalimdb.database.HistoryDatabase
import com.marius.personalimdb.ui.tvShows.details.TvShowDetailsActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


/**
 * This service will retrieve the user's watchlist and then will check the database to see if the last aired episode has changed.
 * In this case, it means that a new episode has aired since the last time the service was called.
 * We send a notification to the user informing him of the new episode.
 */
class NotificationService : IntentService("Episodes Notification") {

    val tvShowList = mutableListOf<TvShow>()
    var totalPages = 1

    override fun onHandleIntent(intent: Intent?) {
        try {
            Log.d(TAG, "Service was called")
            getWatchlistTvShows(1)
        } catch (e: Exception) {
            Log.d(TAG, e.stackTrace.toString())
        }
    }

    private fun showNotification(id: Int, name: String, poster: String?) {
        //url for the photo used as icon for the notification
        val url = "http://image.tmdb.org/t/p/w400${poster}"
        //layout of the custom notification
        val collapsedView = RemoteViews(
            packageName,
            R.layout.notification_small
        )
        //get image from url
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    //set the notification image and title
                    collapsedView.setImageViewBitmap(R.id.image, resource)
                    collapsedView.setTextViewText(R.id.title, name)
                    collapsedView.setTextViewText(R.id.info,"A new episode is out")
                    //intent for the tv show details where we go on notification click
                    //when clicked we update the database with the new info,hence the extra notification
                    val intent =
                        Intent(applicationContext, TvShowDetailsActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            putExtra("tvShowId", id)
                            putExtra("notification", "delete")
                        }
                    //pending intent needed for the notification
                    val pendingIntent: PendingIntent =
                        PendingIntent.getActivity(applicationContext, id, intent, 0)
                    //the actual notification properties
                    val notification =
                        NotificationCompat.Builder(applicationContext, "Episodes_47")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                            .setCustomContentView(collapsedView)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build()
                    val mNotifyManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    mNotifyManager.notify(id, notification)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })
    }


    //this will add all the tv shows into the list of tv shows.
    //when all shows are added we begin comparing the old data with the current one
    private fun getWatchlistTvShows(page: Int) {
        if (page > totalPages) {
            checkOldDates()
            return
        }
        Account.details.sessionId?.let { session ->
            AccountRepository.getWatchlistTvShows(session, page) { details ->
                tvShowList.addAll(details.tvShowList)
                totalPages = details.totalPages
                getWatchlistTvShows(page + 1)
            }
        }
    }

    private fun checkOldDates() {
        //instance of the database where we stored the data of the shows
        val db = Room.databaseBuilder(
            applicationContext,
            WatchlistDatabase::class.java, "air_dates"
        ).fallbackToDestructiveMigration().build()

        tvShowList.forEach { tvShow ->
            //retrieve the new lastAirDate for each show
            TvShowRepository.getTvShowLastAirDate(tvShow.id) { lastAirDate ->
                lastAirDate?.let {
                    thread {
                        //retrieve the data we have about the show
                        val dbShow = db.tvShowDao().getTvShowDate(tvShow.id)
                        //if the show is in our DB then we can compare the data
                        if (dbShow != null) {
                            //we parse the old and new dates
                            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                            val newDate = parser.parse(lastAirDate)
                            val lastDate = parser.parse(dbShow.lastAirDate)
                            newDate?.let {
                                //if the new date is different from the last date and we follow the episodes
                                if (newDate.after(lastDate) && dbShow.followed) {
                                    //send a notification
                                    showNotification(dbShow.id, tvShow.name, tvShow.poster)
                                }
                            }
                        }
                        //Probably useless now since I added this feature when the show is added to the watchlist
                        /*else {//if the show is not in our DB it means that it is a newly added show so we insert it's data
                            db.tvShowDao().insert(
                                TvShowDate(
                                    tvShow.id,
                                    lastAirDate,
                                    true
                                )
                            )
                        }*/
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "MyService"
    }

}