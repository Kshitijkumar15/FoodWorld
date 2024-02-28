package com.example.foodworld.fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodworld.databinding.ActivitySplashBinding
import com.example.foodworld.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<String>,
    private val cartDescription: MutableList<String>,
    private val cartImages: MutableList<String>,
    private val cartQuantity: MutableList<Int>,


    ) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    //instance firebase auth
    private val auth = FirebaseAuth.getInstance()

    //initialising firebase auth
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = cartItems.size
        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemReference = database.reference.child("user").child(userId).child("CartItem")
    }

    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemReference: DatabaseReference
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.blind(position)
    }

    override fun getItemCount(): Int = cartItems.size
    fun getUpdatedItemQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SuspiciousIndentation")
        fun blind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                cartFoodName.text = cartItems[position]
                cartItemPrice.text = cartItemPrices[position]
                val uriString = cartImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)
                cartQuantity.text = quantity.toString()

                delete.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
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
            cartQuantity[position] = itemQuantities[position]
            binding.cartQuantity.text = itemQuantities[position].toString()
        }

        private fun delete(position: Int) {
            val positionRetrieve = position
            getUniquekeyAtPosition(positionRetrieve) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(uniqueKey, position)
                }
            }
        }

        private fun removeItem(uniqueKey: String, position: Int) {
            if (uniqueKey != null) {
                cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartItemPrices.removeAt(position)
                    cartImages.removeAt(position)
                    cartDescription.removeAt(position)
                    cartQuantity.removeAt(position)
                    Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
                    //updating item quantity
                    itemQuantities =
                        itemQuantities.filterIndexed { index, i -> index != position }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed To Delete", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniquekeyAtPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
            cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        private fun increase(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                cartQuantity[position] = itemQuantities[position]
                binding.cartQuantity.text = itemQuantities[position].toString()
            }

        }
    }
}

