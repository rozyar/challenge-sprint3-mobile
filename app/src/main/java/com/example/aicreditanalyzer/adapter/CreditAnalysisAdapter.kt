package com.example.aicreditanalyzer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.model.CreditAnalysis
import java.text.SimpleDateFormat
import java.util.*

class CreditAnalysisAdapter(private val context: Context, private val analysesList: List<CreditAnalysis>) : BaseAdapter() {

    override fun getCount(): Int = analysesList.size

    override fun getItem(position: Int): Any = analysesList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertViewParam: View?, parent: ViewGroup?): View {
        val convertView = convertViewParam ?: LayoutInflater.from(context).inflate(R.layout.item_credit_analysis, parent, false)

        val analysis = analysesList[position]

        val scoreTextView = convertView.findViewById<TextView>(R.id.textViewScore)
        val resultTextView = convertView.findViewById<TextView>(R.id.textViewResult)
        val dateTextView = convertView.findViewById<TextView>(R.id.textViewDate)

        scoreTextView.text = "Score: ${analysis.score}"
        resultTextView.text = analysis.result

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateString = dateFormat.format(Date(analysis.timestamp))

        dateTextView.text = dateString

        return convertView
    }
}
