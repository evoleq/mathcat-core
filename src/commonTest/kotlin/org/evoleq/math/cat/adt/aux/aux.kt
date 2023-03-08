package org.evoleq.math.cat.adt.aux

import org.evoleq.math.cat.aux.`$`
import kotlin.test.Test
import kotlin.test.assertEquals

class AuxTest {
    @Test
    fun evalOn() {
        val f: (Int)->String = {it.toString()}
        val x = 2
        val r = f `$` x
        
        assertEquals("2", r)
    }
}