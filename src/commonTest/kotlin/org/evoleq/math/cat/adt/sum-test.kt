package org.evoleq.math.cat.adt

import kotlin.test.Test
import kotlin.test.assertEquals


class SumTest {
    @Test
    fun `basic sum tests`() {
        assertEquals(true, Sum.iota1<Int,String>()(0) is Sum.Summand1)
    }
    
}