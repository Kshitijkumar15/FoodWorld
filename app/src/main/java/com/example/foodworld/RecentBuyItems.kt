package com.example.foodworld

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodworld.databinding.ActivityRecentBuyItemsBinding
import com.example.foodworld.fragment.adapter.RecentBuyAdapter
import com.example.foodworld.model.OrderDetails

// ... (other imports)

class RecentBuyItems : AppCompatActivity() {

    private val binding: ActivityRecentBuyItemsBinding by lazy {
        ActivityRecentBuyItemsBinding.inflate(layoutInflater)
    }

    private lateinit var allFoodNames: MutableList<String>
    private lateinit var allFoodImages: MutableList<String>
    private lateinit var allFoodPrices: MutableList<String>
    private lateinit var allFoodQuantities: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize the lists here to avoid lateinit property not initialized error
        allFoodNames = mutableListOf()
        allFoodImages = mutableListOf()
        allFoodPrices = mutableListOf()
        allFoodQuantities = mutableListOf()

        val recentOrderItems = intent.getSerializableExtra("RecentBuyItems") as ArrayList<OrderDetails>?
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[0]
                allFoodNames = recentOrderItem.foodNames?.toMutableList() ?: mutableListOf()
                allFoodImages = recentOrderItem.foodImages?.toMutableList() ?: mutableListOf()
                allFoodPrices = recentOrderItem.foodPrices?.toMutableList() ?: mutableListOf()
                allFoodQuantities = recentOrderItem.foodQuantities?.toMutableList() ?: mutableListOf()
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(this@RecentBuyItems)
            adapter = RecentBuyAdapter(
                this@RecentBuyItems,
                allFoodNames,
                allFoodImages,
                allFoodPrices,
                allFoodQuantities
            )
        }
    }
}


