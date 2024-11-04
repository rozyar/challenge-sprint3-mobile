package com.example.aicreditanalyzer.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.adapter.CreditPredictionAdapter
import com.example.aicreditanalyzer.model.CreditPrediction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CreditPredictionListActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var emptyTextView: TextView
    private lateinit var predictionsList: MutableList<CreditPrediction>
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_prediction_list)

        listView = findViewById(R.id.listViewPredictions)
        emptyTextView = findViewById(R.id.emptyTextView)
        predictionsList = mutableListOf()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference("credit_predictions").child(userId)
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    predictionsList.clear()
                    for (data in snapshot.children) {
                        val prediction = data.getValue(CreditPrediction::class.java)
                        if (prediction != null) {
                            predictionsList.add(prediction)
                        }
                    }

                    // Sort the list by timestamp in descending order
                    predictionsList.sortByDescending { it.timestamp }

                    if (predictionsList.isEmpty()) {
                        // Show a message that there are no predictions
                        emptyTextView.text = "Nenhuma previsão encontrada. Por favor, faça uma previsão na página inicial."
                        emptyTextView.visibility = View.VISIBLE
                        listView.visibility = View.GONE
                        Toast.makeText(this@CreditPredictionListActivity, "Nenhuma previsão encontrada. Por favor, faça uma previsão na página inicial.", Toast.LENGTH_LONG).show()
                    } else {
                        emptyTextView.visibility = View.GONE
                        listView.visibility = View.VISIBLE
                        val adapter = CreditPredictionAdapter(this@CreditPredictionListActivity, predictionsList)
                        listView.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CreditPredictionListActivity, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                }
            })

            listView.setOnItemClickListener { _, _, position, _ ->
                val selectedPrediction = predictionsList[position]
                val intent = Intent(this, CreditPredictionDetailActivity::class.java)
                intent.putExtra("predictionId", selectedPrediction.id)
                startActivity(intent)
            }
        } else {
            // Redirect to Login if userId is null
            Toast.makeText(this, "Usuário não logado", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
