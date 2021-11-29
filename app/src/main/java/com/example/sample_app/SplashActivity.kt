package com.example.sample_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.sample_app.app.App
import com.example.sample_app.ui.homemain.view.HomeActivity
import com.example.sample_app.ui.login.view.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launch {
            delay(3000)
        }

        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        Log.e("Nive ", "onCreate:Splash " + pref.getString(App.isLogin, ""))

        startActivity(Intent(this, LoginActivity::class.java))


        if (pref.getString(App.isLogin, "")!!.isEmpty()||pref.getString(App.isLogin, "").equals("0")) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            startActivity(Intent(this, HomeActivity::class.java))

        }

    }
}