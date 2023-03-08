package org.evoleq.math.cat.test

import kotlinx.coroutines.runBlocking

actual fun runTest(block: suspend () -> Unit) = runBlocking{
    block()
}