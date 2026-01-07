package com.aryandi.paymentnfc.data.network

import com.aryandi.paymentnfc.data.dto.TransactionDto
import io.ktor.client.HttpClient

interface HomeApiService {
    suspend fun getTransfers(): Result<List<TransactionDto>>
}

open class HomeApiServiceImpl(httpClient: HttpClient) : ApiService(httpClient), HomeApiService {
    
    override suspend fun getTransfers(): Result<List<TransactionDto>> {
        return get<List<TransactionDto>>(
            endpoint = "${ApiConstant.BASE_URL}${ApiConstant.TRANSFER_LIST}"
        )
    }
}
