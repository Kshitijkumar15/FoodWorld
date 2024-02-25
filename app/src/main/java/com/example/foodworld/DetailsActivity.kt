package com.example.foodworld

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.foodworld.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodImageUrl: String? = null
    private var foodDescription: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodName = intent.getStringExtra("MenuItemName")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodImageUrl = intent.getStringExtra("MenuItemImage")

        with(binding) {
            detailFoodName.text = foodName
            description.text = foodDescription
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImageUrl)).into(detailFoodImage)
        }

    }
}