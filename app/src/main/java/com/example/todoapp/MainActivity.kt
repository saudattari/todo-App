package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.fragments.AddPopUp2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class MainActivity : AppCompatActivity(),AddPopUp2.ShareData,rvAdapter.ClickItems {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var x :Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        recyclerView = binding.rv
        recyclerView.layoutManager = LinearLayoutManager(this)
        databaseReference = FirebaseDatabase.getInstance().reference
        binding.Add.setOnClickListener{ showDialog() }
        retrieveValues()
        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,SignupActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if(isMorning()){
            binding.DayTime.text = getString(R.string.morning)
        }
        else{
            binding.DayTime.text = getString(R.string.Evening)
        }

    }
    private fun isMorning():Boolean {
        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        return hour in 1..11
    }

    private fun showDialog() {
        val fragDialog = AddPopUp2()
        fragDialog.show(supportFragmentManager, "AddPopUp2")
    }


    override fun passData(data: String) {
        setValues(data)
    }

    private fun currentUser():FirebaseUser {
        val currentUser = auth.currentUser
        return currentUser!!
    }

    private fun setValues(data:String) {
        val current = currentUser()
        current.let { user->
        val key1 = databaseReference.child("Users").child(user.uid).child("Tasks").push().key
            if(key1!!.isNotEmpty()){
                databaseReference.child("Users").child(user.uid).child("Tasks").child(key1.toString())
                    .setValue(rvDataModel(data.toString(), false,key1.toString()))
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Data Added Successfully", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this, "Data Insertion Failed", Toast.LENGTH_SHORT).show()
                        }
                        
                    }
            }
        }
        retrieveValues()

    }

    private fun retrieveValues() {
        currentUser().let { user->
            val kerRef = databaseReference.child("Users").child(user.uid).child("Tasks")
            kerRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        val list = mutableListOf<rvDataModel>()
                    for(datashot in snapshot.children){
                        val data = datashot.getValue(rvDataModel::class.java)
                        data.let {
                            list.add(it!!)
                        }
                    }
                    recyclerView.adapter = rvAdapter(list, this@MainActivity, this@MainActivity)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    override fun isComplete1(view:View,data: rvDataModel) {
        currentUser().let { user->
            databaseReference.child("Users").child(user.uid).child("Tasks").child(data.refKey).child("complete")
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val currentValue = snapshot.getValue(Boolean::class.java)
                        if (currentValue != true) {
                            databaseReference.child("Users").child(user.uid).child("Tasks").child(data.refKey).child("complete").setValue(true)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        view.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.green))
                                    }
                                }
                        }
                        else {
                            databaseReference.child("Users").child(user.uid).child("Tasks").child(data.refKey).child("complete").setValue(false)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        view.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.bg))
                                    }
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }

    }


