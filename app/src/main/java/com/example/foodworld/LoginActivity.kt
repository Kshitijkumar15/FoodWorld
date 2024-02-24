package com.example.foodworld

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodworld.databinding.ActivityLoginBinding
import com.example.foodworld.databinding.ActivitySignBinding
import com.example.foodworld.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var database: DatabaseReference

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

        binding.signin.setOnClickListener {
            email = binding.loginemail.text.toString().trim()
            password = binding.loginpass.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        binding.signup.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { createTask ->
            if (createTask.isSuccessful) {
                // Successfully created user
                val user: FirebaseUser? = auth.currentUser
                saveUserData()
                updateUi(user)
            } else {
                // Check if the email is already in use
                if (createTask.exception is FirebaseAuthUserCollisionException) {
                    // Email is already in use, try logging in
                    loginUser(email, password)
                } else {
                    // Other authentication failure
                    Toast.makeText(this, "Authentication Failed: ${createTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.d("Account", "Authentication Failed", createTask.exception)
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { loginTask ->
            if (loginTask.isSuccessful) {
                // Successfully logged in
                val user: FirebaseUser? = auth.currentUser
                updateUi(user)
            } else {
                // Authentication failed
                Toast.makeText(this, "Login Failed: ${loginTask.exception?.message}", Toast.LENGTH_SHORT).show()
                Log.d("Login", "Login Failed", loginTask.exception)
            }
        }
    }

    private fun saveUserData() {
        email = binding.loginemail.text.toString().trim()
        password = binding.loginpass.text.toString().trim()
        val user = UserModel(email, password)
        val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}








