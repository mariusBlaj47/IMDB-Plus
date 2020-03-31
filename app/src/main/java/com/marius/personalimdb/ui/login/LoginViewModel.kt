package com.marius.personalimdb.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory.API_KEY
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.model.RequestToken
import com.marius.personalimdb.data.repository.AccountRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginFinished = MutableLiveData<Unit>()
    val loginError = MutableLiveData<String>()
    val triggerSignUp = MutableLiveData<Unit>()
    lateinit var requestToken: String
    lateinit var sessionId: String

    fun onLogin() {
        username.value?.let { user ->
            password.value?.let { password ->
                AccountRepository.logIn(user, password, requestToken) { success, errorMessage ->
                    if (!success) {
                        loginError.value = errorMessage
                    } else {
                        Account.details.username = username.value
                        getSession()
                    }
                }
            }
        }
    }

    private fun getSession() {
        AccountRepository.getSessionId(requestToken){
            Account.details.sessionId = it
            sessionId = it
            getAccountId()
        }
    }

    private fun getAccountId() {
        AccountRepository.getAccountId(sessionId) {
            Account.details.id = it
            loginFinished.value = Unit
        }
    }

    fun getRequestToken() {
        AccountRepository.getRequestToken {
            requestToken = it
        }
    }

    fun onSignUp() {
        triggerSignUp.value = Unit
    }

}