package com.example.newsapp.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.newsapp.R

class slaphscree : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_slaphscree)
        Handler().postDelayed({
            val i = Intent(
                this@slaphscree,
                NewsActivity::class.java
            )
            startActivity(i)
            finish()
        }, 2000)
    }
}