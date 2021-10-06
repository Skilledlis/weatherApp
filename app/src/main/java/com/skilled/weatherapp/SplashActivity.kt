package com.skilled.weatherapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class SplashActivity : AppCompatActivity() {

    private lateinit var splashActivity: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashActivity = findViewById(R.id.splash_activity)
        splashActivity.alpha = 0f
        splashActivity.animate().setDuration(2000).alpha(1f).withEndAction {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}