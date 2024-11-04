package com.example.aicreditanalyzer.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.api.GPTApiService
import com.example.aicreditanalyzer.model.CreditPrediction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CreditPredictionDetailActivity : Activity() {

    private lateinit var amountEditText: EditText
    private lateinit var incomeEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var databaseRef: DatabaseReference

    private var predictionId: String? = null
    private var currentPrediction: CreditPrediction? = null
    private val gptApiService = GPTApiService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_prediction_detail)

        amountEditText = findViewById(R.id.amountEditText)
        incomeEditText = findViewById(R.id.incomeEditText)
        resultTextView = findViewById(R.id.resultTextView)
        updateButton = findViewById(R.id.updateButton)
        deleteButton = findViewById(R.id.deleteButton)

        predictionId = intent.getStringExtra("predictionId")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null && predictionId != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference("credit_predictions").child(userId).child(predictionId!!)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentPrediction = snapshot.getValue(CreditPrediction::class.java)
                    if (currentPrediction != null) {
                        amountEditText.setText(currentPrediction!!.amount)
                        incomeEditText.setText(currentPrediction!!.income)
                        resultTextView.text = currentPrediction!!.result
                    } else {
                        Toast.makeText(this@CreditPredictionDetailActivity, "Previsão não encontrada", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CreditPredictionDetailActivity, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Usuário não logado ou previsão inválida", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        updateButton.setOnClickListener {
            val newAmount = amountEditText.text.toString()
            val newIncome = incomeEditText.text.toString()
            if (newAmount.isEmpty() || newIncome.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            gptApiService.predictCredit(newAmount, newIncome) { result ->
                runOnUiThread {
                    resultTextView.text = result
                    val updatedPrediction = CreditPrediction(
                        id = currentPrediction!!.id,
                        amount = newAmount,
                        income = newIncome,
                        result = result,
                        timestamp = System.currentTimeMillis()
                    )
                    databaseRef.setValue(updatedPrediction).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Previsão atualizada", Toast.LENGTH_SHORT).show()
                            // Redirect back to the list
                            val intent = Intent(this, CreditPredictionListActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Erro ao atualizar previsão", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        deleteButton.setOnClickListener {
            databaseRef.removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Previsão excluída", Toast.LENGTH_SHORT).show()
                    // Redirect back to the list
                    val intent = Intent(this, CreditPredictionListActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao excluir previsão", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
