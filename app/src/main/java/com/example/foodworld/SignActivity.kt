package com.example.foodworld

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
                createAccount(email, password)
            }
        }

        val signinButton: Button = findViewById(R.id.signinButton)
        signinButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()

                    sendEmailVerification()
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

    private fun sendEmailVerification() {
        val firebaseUser = auth.currentUser
        firebaseUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "Verification Email Sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Error Occurred while sending verification email", Toast.LENGTH_SHORT).show()
                Log.e("EmailVerification", "sendEmailVerification: Failure", task.exception)
            }
        }
    }

    //for saving data into the database
    private fun saveUserData() {
        userName = binding.signupUserName.text.toString().trim()
        email = binding.signupEmail.text.toString().trim()
        password = binding.signupPassword.text.toString().trim()
        val user = UserModel(userName, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}
