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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.aicreditanalyzer.model.CreditAnalysis

class CreditAnalysisActivity : Activity() {
    private lateinit var gptApiService: GPTApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_analysis)
        val viewPredictionsButton = findViewById<Button>(R.id.viewAnalysesButton)
        viewPredictionsButton.setOnClickListener {
            val intent = Intent(this, CreditAnalysisListActivity::class.java)
            startActivity(intent)
        }
        gptApiService = GPTApiService(this)

        val scoreEditText = findViewById<EditText>(R.id.scoreEditText)
        val analyzeButton = findViewById<Button>(R.id.analyzeButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        analyzeButton.setOnClickListener {
            val score = scoreEditText.text.toString()

            if (score.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu score.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call GPT API to analyze the credit score
            gptApiService.analyzeCredit(score) { result ->
                runOnUiThread {
                    resultTextView.text = result

                    // Save the analysis result to Firebase
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val databaseRef = FirebaseDatabase.getInstance()
                            .getReference("credit_analyses")
                            .child(userId)

                        val analysisId = databaseRef.push().key
                        if (analysisId != null) {
                            val analysis = CreditAnalysis(
                                id = analysisId,
                                score = score,
                                result = result
                            )
                            databaseRef.child(analysisId).setValue(analysis)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Análise salva com sucesso", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Erro ao salvar análise: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
