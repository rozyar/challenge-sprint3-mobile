package com.example.aicreditanalyzer.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.aicreditanalyzer.R

class HomeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<Button>(R.id.creditAnalysisButton).setOnClickListener {
            val intent = Intent(this, CreditAnalysisActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.creditPredictionButton).setOnClickListener {
            val intent = Intent(this, CreditPredictionActivity::class.java)
            startActivity(intent)
        }
    }
}