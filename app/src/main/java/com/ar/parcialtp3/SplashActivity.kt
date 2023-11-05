package com.ar.parcialtp3


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent;
import android.widget.Button
import com.ar.parcialtp3.fragments.StartFragment


class SplashActivity : AppCompatActivity() {

    lateinit var btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        btn = findViewById(R.id.splash_btn)

        btn.setOnClickListener {

            // Inside your Activity class
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            val fragment = StartFragment() // Instantiate your Fragment here

            // Replace the existing fragment with the new fragment
            fragmentTransaction.replace(R.id.layoutLogin, fragment) // R.id.fragment_container is the ID of the container in your XML layout
            fragmentTransaction.addToBackStack(null) // Optional: This allows the user to navigate back to the previous fragment
            fragmentTransaction.commit()

    }


}

override fun onPause() {
    super.onPause()
    finish()
}








}