package com.example.foodworld.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.foodworld.R
import com.example.foodworld.databinding.ActivityPayOutBinding
import com.example.foodworld.databinding.FragmentProfileBinding
import com.example.foodworld.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
private lateinit var binding:FragmentProfileBinding

private var auth=FirebaseAuth.getInstance()
    private var database=FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        setUserData()

        binding.saveInfoButton.setOnClickListener {
            val name=binding.pname.text.toString()
            val address=binding.paddress.text.toString()
            val email=binding.pemail.text.toString()
            val phone=binding.pphone.text.toString()

             updateUserData(name,address,email,phone)
        }
        return binding.root

    }

    private fun updateUserData(name: String, address: String, email: String, phone: String) {
        val userId=auth.currentUser?.uid
        if(userId !=null){
            val userRef=database.getReference("user").child(userId)
            val userData= hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userRef.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Updated Successfully",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        val userId=auth.currentUser?.uid
        if(userId !=null){
            val userRef=database.getReference("user").child(userId)
            userRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val userProfile=snapshot.getValue(UserModel::class.java)
                        if(userProfile!=null){
                            binding.pname.setText(userProfile.name)
                            binding.paddress.setText(userProfile.address)
                            binding.pemail.setText(userProfile.email)
                            binding.pphone.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    companion object
}