package com.example.foodworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        val signinButton:Button=findViewById(R.id.already)
        signinButton.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}