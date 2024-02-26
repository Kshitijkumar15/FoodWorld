package com.example.foodworld.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodworld.PayOutActivity
import com.example.foodworld.databinding.FragmentCartBinding
import com.example.foodworld.fragment.adapter.CartAdapter
import com.example.foodworld.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImages: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        reteriveCartItems()

        binding.proceedButton.setOnClickListener {
            getOrderItemDetails()
        }
        return binding.root

    }

    private fun getOrderItemDetails() {
        val orderIdReference = database.reference.child("user").child("CartItem")
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodQuantities = cartAdapter.getUpdatedItemQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    //get the cart item to respect list
                    val orderItem = foodSnapshot.getValue(CartItems::class.java)
                    //add item details to list
                    orderItem?.foodName?.let { foodName.add(it) }
                    orderItem?.foodPrice?.let { foodPrice.add(it) }
                    orderItem?.foodDescription?.let { foodDescription.add(it) }
                    orderItem?.foodImage?.let { foodImage.add(it) }
                }
                orderNow(foodName, foodPrice, foodDescription, foodImage, foodQuantities)
            }

            private fun orderNow(
                foodName: MutableList<String>,
                foodPrice: MutableList<String>,
                foodDescription: MutableList<String>,
                foodImage: MutableList<String>,
                foodQuantities: MutableList<Int>
            ) {
                if (isAdded && context != null) {
                    val intent = Intent(requireContext(), PayOutActivity::class.java)
                    intent.putExtra("foodItemName", foodName as ArrayList<String>)
                    intent.putExtra("foodItemPrice", foodPrice as ArrayList<String>)
                    intent.putExtra("foodItemDescription", foodDescription as ArrayList<String>)
                    intent.putExtra("foodItemImage", foodImage as ArrayList<String>)
                    intent.putExtra("foodItemQuantities", foodQuantities as ArrayList<Int>)
                    startActivity(intent)
                }

            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Order canceled", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun reteriveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodRef: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItem")
        //list to store cart items
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodImages = mutableListOf()
        foodDescriptions = mutableListOf()
        quantity = mutableListOf()

        //fetching data from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)
                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodImage?.let { foodImages.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                }
                SetAdapter()
            }

            private fun SetAdapter() {
                cartAdapter = CartAdapter(
                    requireContext(),
                    foodNames,
                    foodPrices,
                    foodImages,
                    foodDescriptions,
                    quantity
                )
                binding.cartRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not Fetched", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
