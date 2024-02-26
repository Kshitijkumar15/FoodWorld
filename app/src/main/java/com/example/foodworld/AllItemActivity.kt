package com.example.foodworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodworld.databinding.ActivityAllItemBinding
import com.example.foodworld.fragment.adapter.AllItemAdapter
import com.example.foodworld.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private  var menuItems:ArrayList<AllMenu> = ArrayList()

    private val binding:ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseReference=FirebaseDatabase.getInstance().reference
        reteriveMenuItem()
        setContentView(binding.root)

    }

    private fun reteriveMenuItem() {
        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("menu")

        //fetch data from db
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear exixting data before populating
            menuItems.clear()
                for(foodSnapshot in snapshot.children){
                    val menuItem=foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                    menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error ${error.message}")
            }


        })
    }
    private fun setAdapter() {
    val adapter=AllItemAdapter(this@AllItemActivity, menuItems,databaseReference)
        binding.recyclerAllItem.layoutManager=LinearLayoutManager(this)
        binding.recyclerAllItem.adapter=adapter
    }
}