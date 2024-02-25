package com.example.foodworld

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodworld.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)

        binding.goToAdmin.setOnClickListener {
            val intent=Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }

        binding.notification.setOnClickListener {
            val bottomSheetDialog = notiFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Quit")
        builder.setMessage("Do you really want to quit?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                finishAffinity()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
        val alert = builder.create()
        alert.show()
    }
}
