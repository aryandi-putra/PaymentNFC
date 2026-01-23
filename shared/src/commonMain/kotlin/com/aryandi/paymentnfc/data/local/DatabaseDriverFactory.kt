package com.aryandi.paymentnfc.data.local

import app.cash.sqldelight.db.SqlDriver

/**
 * Expect declaration for platform-specific database driver factory
 * Each platform (Android and iOS) provides its own implementation
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
