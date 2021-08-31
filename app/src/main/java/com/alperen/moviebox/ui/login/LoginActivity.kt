package com.alperen.moviebox.ui.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.alperen.moviebox.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onDestroy() {
        super.onDestroy()
        makeAppStopped()
    }

    private fun makeAppStopped() {
        val sharedPref = this.getSharedPreferences("", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putBoolean("isAppStarted", false)
        editor?.apply()
    }
}