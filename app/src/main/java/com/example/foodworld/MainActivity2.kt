package com.example.foodworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodworld.databinding.ActivityAddBinding
import com.example.foodworld.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private val binding: ActivityMain2Binding by lazy {
        ActivityMain2Binding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.addMenu.setOnClickListener {
            val intent=Intent(this,AddActivity::class.java)
            startActivity(intent)
        }
    }
}