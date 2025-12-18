package com.aryandi.paymentnfc.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity

object KermitLogger {
    private val logger = Logger.withTag("PaymentNFC")

    fun d(message: String, tag: String? = null, throwable: Throwable? = null) {
        if (tag != null) {
            Logger.withTag(tag).d(message, throwable)
        } else {
            logger.d(message, throwable)
        }
    }

    fun i(message: String, tag: String? = null, throwable: Throwable? = null) {
        if (tag != null) {
            Logger.withTag(tag).i(message, throwable)
        } else {
            logger.i(message, throwable)
        }
    }

    fun w(message: String, tag: String? = null, throwable: Throwable? = null) {
        if (tag != null) {
            Logger.withTag(tag).w(message, throwable)
        } else {
            logger.w(message, throwable)
        }
    }

    fun e(message: String, tag: String? = null, throwable: Throwable? = null) {
        if (tag != null) {
            Logger.withTag(tag).e(message, throwable)
        } else {
            logger.e(message, throwable)
        }
    }

    fun log(
        severity: Severity,
        message: String,
        tag: String? = null,
        throwable: Throwable? = null
    ) {
        when (severity) {
            Severity.Verbose -> if (tag != null) Logger.withTag(tag).v(message, throwable) else logger.v(message, throwable)
            Severity.Debug -> if (tag != null) Logger.withTag(tag).d(message, throwable) else logger.d(message, throwable)
            Severity.Info -> if (tag != null) Logger.withTag(tag).i(message, throwable) else logger.i(message, throwable)
            Severity.Warn -> if (tag != null) Logger.withTag(tag).w(message, throwable) else logger.w(message, throwable)
            Severity.Error -> if (tag != null) Logger.withTag(tag).e(message, throwable) else logger.e(message, throwable)
            Severity.Assert -> if (tag != null) Logger.withTag(tag).a(message, throwable) else logger.a(message, throwable)
        }
    }

    fun logNetworkRequest(
        method: String,
        url: String,
        headers: Map<String, String> = emptyMap()
    ) {
        val headerString = if (headers.isNotEmpty()) {
            "Headers: ${headers.entries.joinToString(", ") { "${it.key}=${it.value}" }}"
        } else {
            ""
        }

        d("Network Request: $method $url $headerString", "Network-Request")
    }

    fun logNetworkResponse(
        statusCode: Int,
        url: String,
        responseTime: Long = 0
    ) {
        val timeString = if (responseTime > 0) {
            "in ${responseTime}ms"
        } else {
            ""
        }

        d("Network Response: $statusCode $url $timeString", "Network-Response")
    }

    fun logNetworkError(
        url: String,
        errorMessage: String,
        throwable: Throwable? = null
    ) {
        e("Network Error: $url - $errorMessage", "Network-Error", throwable)
    }

    fun logCrash(
        errorMessage: String,
        stackTrace: String,
        throwable: Throwable? = null
    ) {
        e("Application Crash: $errorMessage\nStack Trace: $stackTrace", "App-Crash", throwable)
    }
}