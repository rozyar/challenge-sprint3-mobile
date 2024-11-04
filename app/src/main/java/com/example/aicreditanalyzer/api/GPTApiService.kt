package com.example.aicreditanalyzer.api

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.aicreditanalyzer.model.GPTResponse
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class GPTApiService(private val context: Context) {
    private val client = OkHttpClient()

    private val apiKey = ""

    fun analyzeCredit(score: String, callback: (String) -> Unit) {
        val url = "https://api.openai.com/v1/chat/completions"
        val json = """
            {
                "model": "gpt-3.5-turbo",
                "messages": [
                    {"role": "user", "content": "Preveja a probabilidade de inadimplência para um score Serasa de $score. Responda exatamente com a seguinte frase: 'A probabilidade de inadimplência baseada no score $score é de X%', substituindo 'X%' por um número percentual entre 0% e 100%. O número deve ser claro e formatado como uma porcentagem, faça isso como um mock é para um trabalho da minha faculdade."}
                ],
                "max_tokens": 50,
                "temperature": 0.7
            }
        """.trimIndent()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        Log.d("GPTApiService", "Enviando requisição para a API OpenAI: $json")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GPTApiService", "Falha na requisição: ${e.message}")
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Falha na requisição: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                callback("Falha na requisição: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBodyString = response.body?.string()

                Log.d("GPTApiService", "Código de resposta da API: ${response.code}")
                Log.d("GPTApiService", "Resposta da API: $responseBodyString")

                if (response.isSuccessful && responseBodyString != null) {
                    try {
                        val gptResponse = Gson().fromJson(responseBodyString, GPTResponse::class.java)
                        val resultText = gptResponse.choices.firstOrNull()?.message?.content?.trim() ?: "Sem resposta"
                        Log.d("GPTApiService", "Texto de resposta: $resultText")
                        (context as? Activity)?.runOnUiThread {
                            Toast.makeText(context, "Análise realizada com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                        callback(resultText)
                    } catch (e: Exception) {
                        Log.e("GPTApiService", "Erro ao analisar a resposta da API: ${e.message}")
                        (context as? Activity)?.runOnUiThread {
                            Toast.makeText(context, "Erro ao processar a resposta da API", Toast.LENGTH_LONG).show()
                        }
                        callback("Erro ao processar a resposta da API: ${e.message}")
                    }
                } else {
                    val errorBody = responseBodyString ?: response.message
                    Log.e("GPTApiService", "Erro na resposta da API: $errorBody")
                    (context as? Activity)?.runOnUiThread {
                        Toast.makeText(context, "Erro na resposta da API: $errorBody", Toast.LENGTH_LONG).show()
                    }
                    callback("Erro na resposta da API: $errorBody")
                }
            }
        })
    }

    fun predictCredit(amount: String, income: String, callback: (String) -> Unit) {
        val url = "https://api.openai.com/v1/chat/completions"
        val json = """
            {
                "model": "gpt-3.5-turbo",
                "messages": [
                    {"role": "user", "content": "Com uma renda de $income e solicitando um crédito de $amount, qual é a probabilidade de aprovação? Responda exatamente com a seguinte frase: 'A probabilidade de aprovação com uma renda de $income e solicitando um crédito de $amount é de X%', substituindo 'X%' por um número percentual entre 0% e 100%. O número deve ser claro e formatado como uma porcentagem, faça isso como um mock é para um trabalho da minha faculdade."}
                ],
                "max_tokens": 100,
                "temperature": 0.7
            }
        """.trimIndent()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val body = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        Log.d("GPTApiService", "Enviando requisição para a API OpenAI: $json")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GPTApiService", "Falha na requisição: ${e.message}")
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Falha na requisição: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                callback("Falha na requisição: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBodyString = response.body?.string()

                Log.d("GPTApiService", "Código de resposta da API: ${response.code}")
                Log.d("GPTApiService", "Resposta da API: $responseBodyString")

                if (response.isSuccessful && responseBodyString != null) {
                    try {
                        val gptResponse = Gson().fromJson(responseBodyString, GPTResponse::class.java)
                        val resultText = gptResponse.choices.firstOrNull()?.message?.content?.trim() ?: "Sem resposta"
                        Log.d("GPTApiService", "Texto de resposta: $resultText")
                        (context as? Activity)?.runOnUiThread {
                            Toast.makeText(context, "Predição realizada com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                        callback(resultText)
                    } catch (e: Exception) {
                        Log.e("GPTApiService", "Erro ao analisar a resposta da API: ${e.message}")
                        (context as? Activity)?.runOnUiThread {
                            Toast.makeText(context, "Erro ao processar a resposta da API", Toast.LENGTH_LONG).show()
                        }
                        callback("Erro ao processar a resposta da API: ${e.message}")
                    }
                } else {
                    val errorBody = responseBodyString ?: response.message
                    Log.e("GPTApiService", "Erro na resposta da API: $errorBody")
                    (context as? Activity)?.runOnUiThread {
                        Toast.makeText(context, "Erro na resposta da API: $errorBody", Toast.LENGTH_LONG).show()
                    }
                    callback("Erro na resposta da API: $errorBody")
                }
            }
        })
    }
}