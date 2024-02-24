package com.example.foodworld.fragment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodworld.databinding.ActivitySplashBinding
import com.example.foodworld.databinding.CartItemBinding

class CartAdapter(private  val CartItems:MutableList<String>,private  val CartItemPrice:MutableList<String>,private  val CartImage:MutableList<Int>): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private val itemQuantities = IntArray(CartItems.size) { 1 }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.blind(position)
    }

    override fun getItemCount(): Int = CartItems.size


    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SuspiciousIndentation")
        fun blind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                cartItem.text = CartItems[position]
                cartItemPrice.text = CartItemPrice[position]
                cartitemimage.setImageResource(CartImage[position])
                cartQuantity.text = quantity.toString()

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
            binding.cartQuantity.text = itemQuantities[position].toString()
        }

        private fun delete(position: Int) {
            CartItems.removeAt(position)
            CartItemPrice.removeAt(position)
            CartImage.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, CartItems.size)
        }

        private fun increase(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.cartQuantity.text = itemQuantities[position].toString()
            }

        }
    }
}

