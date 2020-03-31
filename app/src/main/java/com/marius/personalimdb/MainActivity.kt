package com.marius.personalimdb

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.service.NotificationService
import com.marius.personalimdb.ui.login.LoginActivity
import com.marius.personalimdb.ui.search.SearchActivity
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    val tvShowList = mutableListOf<TvShow>()
    var totalPages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        createNotificationChannel()
        getUserData()
        setUpDrawer()
        //check if new episodes are out
        startService(Intent(this, NotificationService::class.java))
    }

    private fun createNotificationChannel() {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Episodes_47",
                "Imdb_plus_service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mNotificationManager.createNotificationChannel(channel)
        }
    }

    private fun getUserData() {
        val sharedPrefs = getSharedPreferences("account", Context.MODE_PRIVATE)
        val id = sharedPrefs.getInt("id", -1)
        val username = sharedPrefs.getString("username", "no_user")
        val sessionId = sharedPrefs.getString("sessionId", "no_session")
        if (id != -1) {
            Account.details.sessionId = sessionId
            Account.details.username = username
            Account.details.id = id
        }
    }

    private fun setUpDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_movies,
                R.id.nav_tvShows,
                R.id.nav_actors,
                R.id.nav_watchlist,
                R.id.nav_logout
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setNavBar(navView)
    }

    private fun setNavBar(navView: NavigationView) {
        val layout = navView.getHeaderView(0)
        if (Account.details.username != null) {
            layout.username.visibility = View.VISIBLE
            layout.username.text = Account.details.username
            layout.loginBtn.visibility = View.GONE
        } else {
            layout.username.visibility = View.GONE
            layout.loginBtn.visibility = View.VISIBLE
            navView.menu.getItem(3).isVisible = false
            navView.menu.getItem(4).isVisible = false
        }
        layout.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_search, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
