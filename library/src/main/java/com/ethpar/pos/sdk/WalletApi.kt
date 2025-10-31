package com.ethpar.pos.sdk

import com.ethpar.pos.sdk.models.PosTransaction
import kotlinx.serialization.json.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface WalletApi {
    @POST("wallet/tx/pos")
    suspend fun executePosTransaction(
        @Body transaction: PosTransaction
    ): JsonObject
}