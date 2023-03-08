package org.evoleq.math.cat.test

import kotlinx.coroutines.*

actual fun runTest(block: suspend () -> Unit)  {
    CoroutineScope(Job()).launch {
        promise {
            block()
        }.await()
    }
}