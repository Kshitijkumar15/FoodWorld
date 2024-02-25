package com.example.foodworld

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.foodworld.databinding.ActivityLoginBinding
import com.example.foodworld.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = Firebase.auth
        database = Firebase.database.reference

        //google signin
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)

        binding.signin.setOnClickListener {
            email = binding.loginemail.text.toString().trim()
            password = binding.loginpass.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
        binding.googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.signup.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { createTask ->
            if (createTask.isSuccessful) {
                // Successfully created user
                val user: FirebaseUser? = auth.currentUser
                saveUserData()
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
// Check if the email is already in use
                    if (createTask.exception is FirebaseAuthUserCollisionException) {
                        // Email is already in use, try logging in
                        loginUser(email, password)
                    } else {
                        // Other authentication failure
                        Toast.makeText(
                            this,
                            "Authentication Failed: ${createTask.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Account", "Authentication Failed", createTask.exception)
                    }
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
                Toast.makeText(
                    this, "Login Failed: ${loginTask.exception?.message}", Toast.LENGTH_SHORT
                ).show()
                Log.d("Login", "Login Failed", loginTask.exception)
            }
        }
    }

    // Send email verification

    private fun saveUserData() {
        email = binding.loginemail.text.toString().trim()
        password = binding.loginpass.text.toString().trim()
        val user = UserModel(email, password)
        val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("user").child(it).setValue(user)
        }
    }

    //Launcher for Google signIn
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task =
                    com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(
                        result.data
                    )
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount?= task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            //successfully singed in with google account
                            Toast.makeText(
                                this, "Successfully signed-in with Google", Toast.LENGTH_SHORT
                            ).show()
                            updateUi(authTask.result?.user)
                            finish()
                        } else {
                            Toast.makeText(this, "Google signed-in Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Google signed-in Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

