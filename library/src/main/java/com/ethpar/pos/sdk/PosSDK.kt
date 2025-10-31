package com.ethpar.pos.sdk

import android.util.Log
import com.ethpar.pos.sdk.models.PosTransaction
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class PosSDK {
    private var baseUrl = "https://api.dev.rampatm.net/ramp/"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
        encodeDefaults = true
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .callTimeout(90, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()

    private val api = retrofit.create(WalletApi::class.java)

    suspend fun executePosTransaction(transaction: PosTransaction): JsonObject =
        withContext(Dispatchers.IO) {
            try {
                Log.d("WalletService", "Sending: $transaction")
                val response = api.executePosTransaction(transaction)
                Log.d("WalletService", "Success: $response")
                response
            } catch (e: Exception) {
                Log.e("WalletService", "Transaction failed", e)
                throw e
            }
        }
}