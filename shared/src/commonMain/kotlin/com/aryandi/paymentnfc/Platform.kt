package com.aryandi.paymentnfc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform