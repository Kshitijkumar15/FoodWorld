package com.example.foodworld.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodworld.databinding.BuyagainBinding

class BuyAdapter(private val buyAgainFoodName:ArrayList<String> ,private val buyAgainFoodPrice:ArrayList<String>,
                 private val buyAgainFoodImage:ArrayList<Int>) : RecyclerView.Adapter<BuyAdapter.BuyAgainViewHolder>(){


    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position],buyAgainFoodImage[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding=BuyagainBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int =buyAgainFoodName.size
    class BuyAgainViewHolder (private  val binding:BuyagainBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(foodName: String, foodPrice: String, foodImage: Int) {
        binding.buyAgainFoodName.text=foodName
            binding.buyAgainFoodPrice.text=foodPrice
            binding.buyAgainFoodImage.setImageResource(foodImage)
        }

    }

}