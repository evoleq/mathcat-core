package org.evoleq.math.cat.adt

import org.evoleq.math.cat.adt.Sum.Companion.iota1
import org.evoleq.math.cat.adt.Sum.Companion.iota2
import org.evoleq.math.cat.adt.Sum.Companion.merge
import org.evoleq.math.cat.aux.o
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SumTest {
    @Test
    fun `basic sum tests`() {
        assertEquals(true, Sum.iota1<Int,String>()(0) is Sum.Summand1)
    }


    @Test
    fun merge() {
        val s11 = merge<String, Int, Boolean>() o iota1<Sum<String,Int>, Sum<String, Boolean>>() o iota1<String, Int>()
        val s12 = merge<String, Int, Boolean>() o iota1<Sum<String,Int>, Sum<String, Boolean>>() o iota2<String, Int>()
        val s22 = merge<String, Int, Boolean>() o iota2<Sum<String,Int>, Sum<String, Boolean>>() o iota2<String, Boolean>()
        val s21 = merge<String, Int, Boolean>() o iota2<Sum<String,Int>, Sum<String, Boolean>>() o iota1<String, Boolean>()

        assertTrue{ s11("") is Sum.Summand1 }
        assertTrue{ s12(0) is Sum.Summand2 }
        assertTrue(  (s12(0) as Sum.Summand2).value is Sum.Summand1)
        assertTrue{ s21("") is Sum.Summand1 }
        assertTrue{ s22(false) is Sum.Summand2 }
        assertTrue{ (s22(false) as Sum.Summand2).value is Sum.Summand2 }
    }
}