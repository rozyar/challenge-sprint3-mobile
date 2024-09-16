package com.example.aicreditanalyzer.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.services.AuthService
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : Activity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.nameEditText).text.toString()
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirmPasswordEditText).text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            AuthService.register(name, email, password, this, onComplete = {
                progressBar.visibility = View.GONE
            })
        }

        val goToLoginButton: Button = findViewById(R.id.goToLoginButton)
        goToLoginButton.setOnClickListener {
            finish()
        }
    }
}
