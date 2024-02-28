package com.example.foodworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.foodworld.databinding.ActivityPayOutBinding
import com.example.foodworld.fragment.CongratsFragment
import com.example.foodworld.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    lateinit var binding: ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var total: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemQuantities: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        //set user data
        setUserData()
        //get user details from firebase
        val intent = intent
        foodItemName = intent.getStringArrayListExtra("foodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("foodItemPrice") as ArrayList<String>
        foodItemDescription =
            intent.getStringArrayListExtra("foodItemDescription") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("foodItemImage") as ArrayList<String>
        foodItemQuantities = intent.getIntegerArrayListExtra("foodItemQuantities") as ArrayList<Int>
        total = calculateTotalAmount().toString()
        Log.d("PayOutActivity", "Total Amount: $total")
        binding.payTotal.setText(total)

        binding.placeOrder.setOnClickListener {
            name = binding.payName.text.toString().trim()
            address = binding.payAddress.text.toString().trim()
            phone = binding.payPhone.text.toString().trim()
            if (name.isBlank() && address.isBlank() && phone.isBlank()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }
        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("orderDetails").push().key
        val orderDetails = OrderDetails(
            userId,
            name,
            foodItemName,
            foodItemPrice,
            foodItemImage,
            foodItemQuantities,
            total,
            address,
            phone,
            time,
            itemPushKey,
            false,
            false
        )

        // Convert 'total' to nullable String before assigning
        orderDetails.totalPrice = total

        val orderRef = databaseReference.child("orderDetails").child(itemPushKey!!)
        orderRef.setValue(orderDetails).addOnSuccessListener {
            val bottomSheetDialogue = CongratsFragment()
            bottomSheetDialogue.show(supportFragmentManager, "Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to order", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {
                // Handle success if needed
            }
    }

    private fun removeItemFromCart() {
        val cartItemRef = databaseReference.child("user").child(userId).child("CartItem")
        cartItemRef.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in 0 until foodItemPrice.size) {
            var price = foodItemPrice[i]
            val priceIntValue: Int
            priceIntValue = price.toInt()
            var quantity = foodItemQuantities[i]
            totalAmount += priceIntValue * quantity
        }

        return totalAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userRef = databaseReference.child("user").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresses = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phone").getValue(String::class.java) ?: ""
                        binding.apply {
                            payName.setText(names)
                            payAddress.setText(addresses)
                            payPhone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error if needed
                }
            })
        }
    }
}
