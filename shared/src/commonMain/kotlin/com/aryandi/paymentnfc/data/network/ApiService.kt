package com.aryandi.paymentnfc.data.network

import com.aryandi.paymentnfc.logging.KermitLogger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

abstract class ApiService(protected val httpClient: HttpClient) {
    
    protected suspend inline fun <reified T> get(
        endpoint: String,
        parameters: Map<String, String> = emptyMap()
    ): Result<T> {
        return runCatching {
            val response = httpClient.get(endpoint) {
                url {
                    parameters.forEach { (key, value) ->
                        this.parameters.append(key, value)
                    }
                }
            }
            response.body<T>()
        }.onFailure { e ->
            KermitLogger.logNetworkError(
                url = endpoint,
                errorMessage = "GET request failed: ${e.message}",
                throwable = e
            )
        }
    }
    
    protected suspend inline fun <reified T, reified B> post(
        endpoint: String,
        body: B
    ): Result<T> {
        return runCatching {
            val response = httpClient.post(endpoint) {
                setBody(body)
                contentType(ContentType.Application.Json)
            }
            response.body<T>()
        }.onFailure { e ->
            KermitLogger.logNetworkError(
                url = endpoint,
                errorMessage = "POST request failed: ${e.message}",
                throwable = e
            )
        }
    }
    
    protected suspend inline fun <reified T, reified B> put(
        endpoint: String,
        body: B
    ): Result<T> {
        return runCatching {
            val response = httpClient.put(endpoint) {
                setBody(body)
                contentType(ContentType.Application.Json)
            }
            response.body<T>()
        }.onFailure { e ->
            KermitLogger.logNetworkError(
                url = endpoint,
                errorMessage = "PUT request failed: ${e.message}",
                throwable = e
            )
        }
    }
    
    protected suspend inline fun <reified T> delete(endpoint: String): Result<T> {
        return runCatching {
            val response = httpClient.delete(endpoint)
            response.body<T>()
        }.onFailure { e ->
            KermitLogger.logNetworkError(
                url = endpoint,
                errorMessage = "DELETE request failed: ${e.message}",
                throwable = e
            )
        }
    }
}