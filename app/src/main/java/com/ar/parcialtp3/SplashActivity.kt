package com.ar.parcialtp3


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent;
import com.ar.parcialtp3.fragments.PreStartFragment


class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, StartActivity::class.java))
        }, 2500)

    }

    override fun onPause() {
        super.onPause()
        finish()
    }








}