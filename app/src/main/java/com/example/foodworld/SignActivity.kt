package com.example.foodworld

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.foodworld.databinding.ActivitySignBinding
import com.example.foodworld.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = Firebase.auth
        database = Firebase.database.reference

        //Google signing
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)

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
        binding.googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }


        val signinButton: Button = findViewById(R.id.signinButton)
        signinButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
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
                Toast.makeText(applicationContext, "Verification Email Sent", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Error Occurred while sending verification email",
                    Toast.LENGTH_SHORT
                ).show()
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

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task =
                    com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(
                        result.data
                    )
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            //successfully singed in with google account
                            Toast.makeText(
                                this,
                                "Successfully signed-in with Google",
                                Toast.LENGTH_SHORT
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
