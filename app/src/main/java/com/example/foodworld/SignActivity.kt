package com.example.foodworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.foodworld.databinding.ActivitySignBinding
import com.example.foodworld.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var database: DatabaseReference


    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        database = Firebase.database.reference
        binding.create.setOnClickListener {
            //getting text from edittext
            userName = binding.signupUserName.text.toString().trim()
            email = binding.signupEmail.text.toString().trim()
            password = binding.signupPassword.text.toString().trim()

            if (userName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            } else {
                createAccout(email, password)
            }

//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }
        val signinButton: Button = findViewById(R.id.signinButton)
        signinButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccout(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Fail", task.exception)
            }
        }
    }

    //for saving data into database
    private fun saveUserData() {
        userName = binding.signupUserName.text.toString().trim()
        email = binding.signupEmail.text.toString().trim()
        password = binding.signupPassword.text.toString().trim()
        val user = UserModel(userName, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}