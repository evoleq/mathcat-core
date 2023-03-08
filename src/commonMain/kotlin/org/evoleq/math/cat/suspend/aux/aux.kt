package org.evoleq.math.cat.suspend.aux

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.evoleq.math.cat.marker.MathCatDsl

@MathCatDsl
suspend infix fun <S, T> (suspend CoroutineScope.(S)-> T).`$`(s: S): T = coroutineScope {  this@`$`(s) }

@MathCatDsl
suspend infix fun <R, S, T> (suspend CoroutineScope.(S)->T).o(other: suspend CoroutineScope.(R)->S): suspend CoroutineScope.(R)->T = { r ->  this@o(other(r)) }