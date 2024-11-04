package com.example.aicreditanalyzer.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.adapter.CreditAnalysisAdapter
import com.example.aicreditanalyzer.model.CreditAnalysis
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CreditAnalysisListActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var emptyTextView: TextView
    private lateinit var analysesList: MutableList<CreditAnalysis>
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_analysis_list)

        listView = findViewById(R.id.listViewAnalyses)
        emptyTextView = findViewById(R.id.emptyTextView)
        analysesList = mutableListOf()

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference("credit_analyses").child(userId)
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    analysesList.clear()
                    for (data in snapshot.children) {
                        val analysis = data.getValue(CreditAnalysis::class.java)
                        if (analysis != null) {
                            analysesList.add(analysis)
                        }
                    }

                    // Sort the list by timestamp in descending order
                    analysesList.sortByDescending { it.timestamp }

                    if (analysesList.isEmpty()) {
                        // Show a message that there are no analyses
                        emptyTextView.text = "Nenhuma análise encontrada. Por favor, faça uma análise na página inicial."
                        emptyTextView.visibility = View.VISIBLE
                        listView.visibility = View.GONE
                        Toast.makeText(this@CreditAnalysisListActivity, "Nenhuma análise encontrada. Por favor, faça uma análise na página inicial.", Toast.LENGTH_LONG).show()
                    } else {
                        emptyTextView.visibility = View.GONE
                        listView.visibility = View.VISIBLE
                        val adapter = CreditAnalysisAdapter(this@CreditAnalysisListActivity, analysesList)
                        listView.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CreditAnalysisListActivity, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                }
            })

            listView.setOnItemClickListener { _, _, position, _ ->
                val selectedAnalysis = analysesList[position]
                val intent = Intent(this, CreditAnalysisDetailActivity::class.java)
                intent.putExtra("analysisId", selectedAnalysis.id)
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
