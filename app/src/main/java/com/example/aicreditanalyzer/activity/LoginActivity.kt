package com.example.aicreditanalyzer.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.services.AuthService
import com.google.firebase.FirebaseApp

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            AuthService.login(email, password, this)
        }

        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
