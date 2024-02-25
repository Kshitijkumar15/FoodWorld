package com.example.foodworld.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodworld.databinding.ActivityMain2Binding
import com.example.foodworld.databinding.ItemBinding

class AllItemAdapter(
    private val MenuItemName: ArrayList<String>,
    private val MenuItemPrice: ArrayList<String>,
    private val MenuItemImage: ArrayList<Int>
) : RecyclerView.Adapter<AllItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = IntArray(MenuItemName.size) { 1 }
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

    override fun getItemCount(): Int = MenuItemName.size
    inner class AddItemViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                foddNameTextView.text = MenuItemName[position]
                foodPriceTextView.text = MenuItemPrice[position]
                foodNameImageView.setImageResource(MenuItemImage[position])
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
            MenuItemName.removeAt(position)
            MenuItemPrice.removeAt(position)
            MenuItemImage.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, MenuItemName.size)
        }

        private fun increase(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.itemQuantity.text = itemQuantities[position].toString()
            }
        }

        }
}