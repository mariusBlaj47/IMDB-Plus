package com.marius.personalimdb.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory.API_KEY
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.model.LoginRequest
import com.marius.personalimdb.data.model.RequestToken
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
                TmdbServiceFactory.tmdbAccountService.logIn(
                    LoginRequest(user, password, requestToken),
                    API_KEY
                ).enqueue(object : Callback<RequestToken> {
                    override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                        Log.d("Login", t.localizedMessage)
                    }

                    override fun onResponse(
                        call: Call<RequestToken>,
                        response: Response<RequestToken>
                    ) {
                        if (response.code() == 200) {
                            Account.details.username = username.value
                            getSession()
                        } else {
                            response.errorBody()?.let {
                                val gson = Gson()
                                val error = gson.fromJson(it.charStream(), RequestToken::class.java)
                                loginError.value = error.message
                            }

                        }
                    }

                })
            }
        }
    }

    private fun getSession() {
        TmdbServiceFactory.tmdbAccountService.getSessionId(
            RequestToken(value = requestToken),
            API_KEY
        ).enqueue(object : Callback<RequestToken> {
            override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                Log.d("Login", t.localizedMessage)
            }

            override fun onResponse(call: Call<RequestToken>, response: Response<RequestToken>) {
                response.body()?.let {
                    it.sessionId?.let {
                        Account.details.sessionId = it
                        sessionId = it
                        getAccountId()
                    }
                }
            }

        })
    }

    private fun getAccountId() {
        TmdbServiceFactory.tmdbAccountService.getAccount(sessionId, API_KEY)
            .enqueue(object : Callback<RequestToken> {
                override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                    Log.d("Login", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<RequestToken>,
                    response: Response<RequestToken>
                ) {
                    response.body()?.let {
                        it.id?.let {
                            Account.details.id = it
                            loginFinished.value = Unit
                        }
                    }
                }

            })
    }

    fun getRequestToken() {
        TmdbServiceFactory.tmdbAccountService.getRequestToken(API_KEY)
            .enqueue(object : Callback<RequestToken> {
                override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                    Log.d("Login", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<RequestToken>,
                    response: Response<RequestToken>
                ) {
                    response.body()?.let {
                        it.value?.let {
                            requestToken = it
                        }
                    }
                }

            })
    }

    fun onSignUp() {
        triggerSignUp.value = Unit
    }

}