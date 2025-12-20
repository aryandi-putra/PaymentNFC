package com.aryandi.paymentnfc.data.network

import com.aryandi.paymentnfc.data.dto.TransactionDto
import io.ktor.client.HttpClient

class HomeApiService(httpClient: HttpClient) : ApiService(httpClient) {
    
    suspend fun getTransfers(): Result<List<TransactionDto>> {
        return get<List<TransactionDto>>(
            endpoint = "https://paymentnfc.free.beeceptor.com/transfer_list"
        )
    }
}
