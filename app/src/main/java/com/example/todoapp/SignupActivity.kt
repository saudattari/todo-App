package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todoapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
        private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.logBtn.setOnClickListener {
            signIn()
        }
        binding.textView9.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    private fun signIn() {
        val eMail = binding.loginMailEdit.text.toString()
        val password = binding.logInPasswordEdit.text.toString()
        if(eMail.isNotEmpty() && password.isNotEmpty() && binding.editTextText.text.toString().isNotEmpty()) {
            if (binding.editTextTextPassword2.text.toString() == password) {
                auth.createUserWithEmailAndPassword(eMail, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Registration Completed", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Check your Connection", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                Toast.makeText(this, "Sorry Both Passwords Not Matched", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Fill All fields", Toast.LENGTH_SHORT).show()
        }
    }
}