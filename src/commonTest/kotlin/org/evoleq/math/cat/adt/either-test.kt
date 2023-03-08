package org.evoleq.math.cat.adt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EitherTest {

    @Test
    fun mapLeft() {

        val either = Left<Int, Nothing>(1)
        val result = either mapLeft { it + 1 }
        println(result)

        assertTrue{ result is Left }
        assertEquals(2 ,(result as Left).value)
    }

}