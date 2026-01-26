package com.aryandi.paymentnfc.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.aryandi.paymentnfc.database.PaymentNFCDatabase

/**
 * Android implementation of DatabaseDriverFactory
 * Uses AndroidSqliteDriver with Context
 */
actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = PaymentNFCDatabase.Schema,
            context = context,
            name = "paymentnfc.db"
        )
    }
}
