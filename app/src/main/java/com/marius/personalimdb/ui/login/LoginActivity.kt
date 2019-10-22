package com.marius.personalimdb.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.marius.personalimdb.MainActivity
import com.marius.personalimdb.R
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_login
            )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.getRequestToken()

        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.loginFinished.observe(this, Observer {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            saveData()
            startActivity(intent)
        })
        viewModel.loginError.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.triggerSignUp.observe(this, Observer {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.themoviedb.org/account/signup")
                )
            )
        })
    }

    private fun saveData() {
        val sharedPrefs = getSharedPreferences("account", Context.MODE_PRIVATE).edit()
        sharedPrefs.putString("username", Account.details.username)
        Account.details.id?.let {
            sharedPrefs.putInt("id", it)
        }
        sharedPrefs.putString("sessionId", Account.details.sessionId)
        sharedPrefs.apply()
    }
}
