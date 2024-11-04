package com.example.aicreditanalyzer.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.api.GPTApiService
import com.example.aicreditanalyzer.model.CreditAnalysis
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CreditAnalysisDetailActivity : Activity() {

    private lateinit var scoreEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var databaseRef: DatabaseReference

    private var analysisId: String? = null
    private var currentAnalysis: CreditAnalysis? = null
    private val gptApiService = GPTApiService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_analysis_detail)

        scoreEditText = findViewById(R.id.scoreEditText)
        resultTextView = findViewById(R.id.resultTextView)
        updateButton = findViewById(R.id.updateButton)
        deleteButton = findViewById(R.id.deleteButton)

        analysisId = intent.getStringExtra("analysisId")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null && analysisId != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference("credit_analyses").child(userId).child(analysisId!!)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentAnalysis = snapshot.getValue(CreditAnalysis::class.java)
                    if (currentAnalysis != null) {
                        scoreEditText.setText(currentAnalysis!!.score)
                        resultTextView.text = currentAnalysis!!.result
                    } else {
                        Toast.makeText(this@CreditAnalysisDetailActivity, "Análise não encontrada", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CreditAnalysisDetailActivity, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Usuário não logado ou análise inválida", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        updateButton.setOnClickListener {
            val newScore = scoreEditText.text.toString()
            if (newScore.isEmpty()) {
                Toast.makeText(this, "Por favor, insira o score", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            gptApiService.analyzeCredit(newScore) { result ->
                runOnUiThread {
                    resultTextView.text = result
                    val updatedAnalysis = CreditAnalysis(
                        id = currentAnalysis!!.id,
                        score = newScore,
                        result = result,
                        timestamp = System.currentTimeMillis()
                    )
                    databaseRef.setValue(updatedAnalysis).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Análise atualizada", Toast.LENGTH_SHORT).show()
                            // Redirect back to the list
                            val intent = Intent(this, CreditAnalysisListActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Erro ao atualizar análise", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        deleteButton.setOnClickListener {
            databaseRef.removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Análise excluída", Toast.LENGTH_SHORT).show()
                    // Redirect back to the list
                    val intent = Intent(this, CreditAnalysisListActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Erro ao excluir análise", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
