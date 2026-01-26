package com.aryandi.paymentnfc.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.aryandi.paymentnfc.database.PaymentNFCDatabase

/**
 * iOS implementation of DatabaseDriverFactory
 * Uses NativeSqliteDriver for iOS/Native targets
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = PaymentNFCDatabase.Schema,
            name = "paymentnfc.db"
        )
    }
}
