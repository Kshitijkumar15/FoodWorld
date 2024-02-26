package com.example.foodworld.fragment.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodworld.databinding.ItemBinding
import com.example.foodworld.model.AllMenu
import com.google.firebase.database.DatabaseReference

class AllItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference
) : RecyclerView.Adapter<AllItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = IntArray(menuList.size) { 1 }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllItemAdapter.AddItemViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllItemAdapter.AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size
    inner class AddItemViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                val menuItem=menuList[position]
                val uriString=menuItem.foodImage
                val uri= Uri.parse(uriString)

                foddNameTextView.text = menuItem.foodName
                foodPriceTextView.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(foodNameImageView)
                itemQuantity.text = quantity.toString()
                delete.setOnClickListener {
                    val itemPosition=adapterPosition
                    if(itemPosition!=RecyclerView.NO_POSITION){
                        delete(position)
                    }
                }

                plus.setOnClickListener {
                    increase(position)
                }
                minus.setOnClickListener {
                    decrease(position)
                }
            }
        }

        private fun decrease(position: Int) {
            if (itemQuantities[position] > 1)
                itemQuantities[position]--
            binding.itemQuantity.text = itemQuantities[position].toString()
        }

        private fun delete(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }

        private fun increase(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.itemQuantity.text = itemQuantities[position].toString()
            }
        }

        }
}