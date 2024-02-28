package com.example.foodworld

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodworld.databinding.ActivityRecentBuyItemsBinding
import com.example.foodworld.fragment.adapter.RecentBuyAdapter
import com.example.foodworld.model.OrderDetails

class RecentBuyItems : AppCompatActivity() {
    private val binding: ActivityRecentBuyItemsBinding by lazy {
        ActivityRecentBuyItemsBinding.inflate(layoutInflater)
    }
    private lateinit var allFoodNames: ArrayList<String>
    private lateinit var allFoodImages: ArrayList<String>
    private lateinit var allFoodPrices: ArrayList<String>
    private lateinit var allFoodQuantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize the lists here to avoid lateinit property not initialized error
        allFoodNames = ArrayList()
        allFoodImages = ArrayList()
        allFoodPrices = ArrayList()
        allFoodQuantities = ArrayList()

        val recentOrderItems = intent.getSerializableExtra("RecentBuyItems") as ArrayList<OrderDetails>?
        recentOrderItems?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[0]
                allFoodNames = recentOrderItem.foodNames?.let { ArrayList(it) } ?: ArrayList()
                allFoodImages = recentOrderItem.foodImages?.let { ArrayList(it) } ?: ArrayList()
                allFoodPrices = recentOrderItem.foodPrices?.let { ArrayList(it) } ?: ArrayList()
                allFoodQuantities = recentOrderItem.foodQuantities?.let { ArrayList(it) } ?: ArrayList()
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.recyclerView
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this, allFoodNames, allFoodImages, allFoodPrices, allFoodQuantities)
        rv.adapter = adapter
    }
}
