package com.example.foodworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodworld.databinding.ActivityMainBinding
import com.example.foodworld.databinding.ActivityPayOutBinding
import com.example.foodworld.fragment.CongratsFragment

class PayOutActivity : AppCompatActivity() {
    lateinit var binding:ActivityPayOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.placeOrder.setOnClickListener {
            val bottomSheetDialoge= CongratsFragment()
            bottomSheetDialoge.show(supportFragmentManager,"Test")
        }
    }
}