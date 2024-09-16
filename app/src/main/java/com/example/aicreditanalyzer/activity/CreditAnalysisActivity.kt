package com.example.aicreditanalyzer.activity


import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.api.GPTApiService

class CreditAnalysisActivity : Activity() {
    private lateinit var gptApiService: GPTApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_analysis)

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

            gptApiService.analyzeCredit(score) { result ->
                runOnUiThread {
                    resultTextView.text = result
                }
            }
        }
    }
}
