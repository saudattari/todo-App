package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todoapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.logBtn.setOnClickListener {
            signIn()
        }
        binding.registerNow.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
            finish()
        }
    }

    private fun signIn() {
        val eMail = binding.loginMailEdit.text.toString()
        val password = binding.logInPasswordEdit.text.toString()
        if(eMail.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(eMail, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Wrong Credentials", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else{
                        Toast.makeText(this, "Fill All fields", Toast.LENGTH_SHORT).show()
        }
    }
}