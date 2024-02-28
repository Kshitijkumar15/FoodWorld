package com.example.foodworld.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodworld.RecentBuyItems
import com.example.foodworld.databinding.FragmentHistoryBinding
import com.example.foodworld.fragment.adapter.BuyAdapter
import com.example.foodworld.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// ... (other imports)

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItems: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle arguments if needed
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        retrieveBuyHistory()

        binding.recentBuyItem.setOnClickListener {
            seeRecentBuyItem()
        }
        return binding.root
    }

    private fun seeRecentBuyItem() {
        listOfOrderItems.firstOrNull()?.let { recentBuy ->
            val intent = Intent(requireContext(), RecentBuyItems::class.java)
            intent.putExtra("RecentBuyItems", listOfOrderItems)
            startActivity(intent)
        }
    }

    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""
        val buyItemRef = database.reference.child("user").child(userId).child("BuyHistory")
        val sortingQuery = buyItemRef.orderByChild("currentTime")
        sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear or initialize the lists before populating them
                val buyAgainFoodName = mutableListOf<String>()
                val buyAgainFoodPrice = mutableListOf<String>()
                val buyAgainFoodImage = mutableListOf<String>()

                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItems.add(it)
                    }
                }
                listOfOrderItems.reverse()

                if (listOfOrderItems.isNotEmpty()) {
                    setDataInRecentBuy()
                    setPreviousBuyItem(buyAgainFoodName, buyAgainFoodPrice, buyAgainFoodImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled if needed
            }

            private fun setDataInRecentBuy() {
                binding.recentBuyItem.visibility = View.VISIBLE
                val recentOrderItem = listOfOrderItems.firstOrNull()
                recentOrderItem?.let {
                    with(binding) {
                        buyAgainFoodName.text = it.foodNames?.firstOrNull() ?: ""
                        buyAgainFoodPrice.text = it.foodPrices?.firstOrNull() ?: ""
                        val image = it.foodImages?.firstOrNull() ?: ""
                        val uri = Uri.parse(image)
                        Glide.with(requireContext()).load(uri).into(buyAgainFoodImage)
                    }
                }
            }

            private fun setPreviousBuyItem(
                buyAgainFoodName: MutableList<String>,
                buyAgainFoodPrice: MutableList<String>,
                buyAgainFoodImage: MutableList<String>
            ) {
                // Initialize RecyclerView outside the loop
                val rv = binding.buyAgainRecyclerView
                rv.layoutManager = LinearLayoutManager(requireContext())

                for (i in 1 until listOfOrderItems.size) {
                    listOfOrderItems[i].foodNames?.firstOrNull()?.let {
                        buyAgainFoodName.add(it)
                        listOfOrderItems[i].foodPrices?.firstOrNull()?.let {
                            buyAgainFoodPrice.add(it)
                            listOfOrderItems[i].foodImages?.firstOrNull()?.let {
                                buyAgainFoodImage.add(it)
                            }
                        }
                    }
                }

                buyAgainAdapter = BuyAdapter(
                    buyAgainFoodName,
                    buyAgainFoodPrice,
                    buyAgainFoodImage,
                    requireContext()
                )
                rv.adapter = buyAgainAdapter
            }
        })
    }
}
