package com.aryandi.paymentnfc.di

import co.touchlab.kermit.Logger
import com.aryandi.paymentnfc.logging.KermitLogger
import org.koin.dsl.module

val loggingModule = module {
    // Since KermitLogger is an object, we don't need to provide it as a dependency
    // It's globally accessible through the object instance
}