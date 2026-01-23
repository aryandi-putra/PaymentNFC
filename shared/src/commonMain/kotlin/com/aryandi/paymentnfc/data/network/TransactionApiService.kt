package com.aryandi.paymentnfc.data.network

import com.aryandi.paymentnfc.data.dto.TransactionDto
import io.ktor.client.HttpClient

interface TransactionApiService {
    suspend fun getTransfers(): Result<List<TransactionDto>>
}

open class TransactionApiServiceImpl(httpClient: HttpClient) : ApiService(httpClient), TransactionApiService {
    
    override suspend fun getTransfers(): Result<List<TransactionDto>> {
        return get<List<TransactionDto>>(
            endpoint = "${ApiConstant.BASE_URL}${ApiConstant.TRANSFER_LIST}"
        )
    }
}
