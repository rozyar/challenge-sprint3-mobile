package com.example.aicreditanalyzer.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.aicreditanalyzer.activity.HomeActivity
import com.example.aicreditanalyzer.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object AuthService {
    fun login(email: String, password: String, context: Context) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (context is Activity) {
                        context.startActivity(Intent(context, HomeActivity::class.java))
                    } else {
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }

                } else {
                    Toast.makeText(context, "Erro ao fazer login", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun register(name: String, email: String, password: String, context: Context, onComplete: () -> Unit) {
        if (password.length < 6) {
            Toast.makeText(context, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show()
            onComplete()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    val database = FirebaseDatabase.getInstance().reference

                    val user = User(name, email)

                    if (userId != null) {
                        database.child("users").child(userId).setValue(user)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, HomeActivity::class.java))
                                onComplete()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Erro ao salvar os dados do usuário.", Toast.LENGTH_SHORT).show()
                                onComplete()
                            }
                    } else {
                        Toast.makeText(context, "Erro ao obter ID do usuário.", Toast.LENGTH_SHORT).show()
                        onComplete()
                    }
                } else {
                    Log.v("Error", task.exception?.message.toString())
                    Toast.makeText(context, "Erro ao registrar usuário: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    onComplete()
                }
            }
    }
}
