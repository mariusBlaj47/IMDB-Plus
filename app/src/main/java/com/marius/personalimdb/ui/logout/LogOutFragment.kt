package com.marius.personalimdb.ui.logout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marius.personalimdb.MainActivity
import com.marius.personalimdb.R
import com.marius.personalimdb.data.Account

class LogOutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_progress50dp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefs = activity?.getSharedPreferences("account", Context.MODE_PRIVATE)?.edit()
        sharedPrefs?.clear()
        sharedPrefs?.apply()
        Account.details.id = null
        Account.details.username = null
        Account.details.sessionId = null
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}