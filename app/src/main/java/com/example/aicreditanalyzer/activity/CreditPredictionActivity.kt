// CreditPredictionActivity.kt
package com.example.aicreditanalyzer.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.api.GPTApiService
import com.example.aicreditanalyzer.model.CreditPrediction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CreditPredictionActivity : Activity() {
    private lateinit var gptApiService: GPTApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_prediction)
        val viewAnalysesButton = findViewById<Button>(R.id.viewPredictionsButton)
        viewAnalysesButton.setOnClickListener {
            val intent = Intent(this, CreditPredictionListActivity::class.java)
            startActivity(intent)
        }

        gptApiService = GPTApiService(this)

        val amountEditText = findViewById<EditText>(R.id.amountEditText)
        val incomeEditText = findViewById<EditText>(R.id.incomeEditText)
        val predictButton = findViewById<Button>(R.id.predictButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        predictButton.setOnClickListener {
            val amount = amountEditText.text.toString()
            val income = incomeEditText.text.toString()

            if (amount.isEmpty() || income.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            gptApiService.predictCredit(amount, income) { result ->
                runOnUiThread {
                    resultTextView.text = result

                    // Save to Firebase
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val databaseRef = FirebaseDatabase.getInstance().getReference("credit_predictions").child(userId)
                        val predictionId = databaseRef.push().key
                        if (predictionId != null) {
                            val prediction = CreditPrediction(
                                id = predictionId,
                                amount = amount,
                                income = income,
                                result = result
                            )
                            databaseRef.child(predictionId).setValue(prediction)
                            Toast.makeText(this, "Predição salva com sucesso", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
