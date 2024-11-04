package com.example.aicreditanalyzer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter
import com.example.aicreditanalyzer.R
import com.example.aicreditanalyzer.model.CreditPrediction
import java.text.SimpleDateFormat
import java.util.*

class CreditPredictionAdapter(private val context: Context, private val predictionsList: List<CreditPrediction>) : BaseAdapter() {

    override fun getCount(): Int = predictionsList.size

    override fun getItem(position: Int): Any = predictionsList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertViewParam: View?, parent: ViewGroup?): View {
        val convertView = convertViewParam ?: LayoutInflater.from(context).inflate(R.layout.item_credit_prediction, parent, false)

        val prediction = predictionsList[position]

        val amountIncomeTextView = convertView.findViewById<TextView>(R.id.textViewAmountIncome)
        val resultTextView = convertView.findViewById<TextView>(R.id.textViewResult)
        val dateTextView = convertView.findViewById<TextView>(R.id.textViewDate)

        amountIncomeTextView.text = "Valor: ${prediction.amount}, Renda: ${prediction.income}"
        resultTextView.text = prediction.result

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateString = dateFormat.format(Date(prediction.timestamp))

        dateTextView.text = dateString

        return convertView
    }
}
