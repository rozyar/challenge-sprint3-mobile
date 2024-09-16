package com.example.aicreditanalyzer.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.api.GPTApiService

class CreditPredictionActivity : Activity(){
    private lateinit var gptApiService: GPTApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_prediction)

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
                }
            }
        }
    }
}