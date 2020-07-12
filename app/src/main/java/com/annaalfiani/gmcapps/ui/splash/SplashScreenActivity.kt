package com.annaalfiani.gmcapps.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.annaalfiani.gmcapps.R
import com.annaalfiani.gmcapps.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity,
                MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)
    }
}
