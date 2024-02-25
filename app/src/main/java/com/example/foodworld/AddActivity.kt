package com.example.foodworld

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodworld.databinding.ActivityAddBinding
import com.example.foodworld.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddActivity : AppCompatActivity() {
    //For food item details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private var foodImageUri: Uri? = null

    // For Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddBinding by lazy {
        ActivityAddBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initialising Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.addButton.setOnClickListener {
            //Getting data from xml
            foodName = binding.enterFoodName.text.toString().trim()
            foodPrice = binding.enterFoodPrice.text.toString().trim()
            foodDescription = binding.descriptionEditView.text.toString().trim()

            if (!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank())) {
                uploadData()
                Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show()
            }

        }

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

    }

    private fun uploadData() {
        val menuRef = database.getReference("menu")
        //generate a unique key for new menu item
        val newItemkey = menuRef.push().key

        if (foodImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_item/${newItemkey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    //creatining new menu item
                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodImage = downloadUrl.toString()
                    )
                    newItemkey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show()

                        }
                            .addOnFailureListener {
                                Toast.makeText(this, "Data Upload Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
                    }

            }

        } else {
            Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectImage.setImageURI(uri)
            foodImageUri=uri
        }
    }
}