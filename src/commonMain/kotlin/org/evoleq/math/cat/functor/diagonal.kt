package org.evoleq.math.cat.functor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.evoleq.math.cat.marker.MathCatDsl

typealias Diagonal<T> = Pair<T, T>

@MathCatDsl
@Suppress("FunctionName")
fun <T> Diagonal(value: T): Diagonal<T> = Pair(value,value)

/**
 * Map a Diagonal
 */
@MathCatDsl
infix fun <S, T> Diagonal<S>.map(f: (S)->T): Diagonal<T> = Diagonal(f(first))

/**
 * Map a Diagonal
 */
@MathCatDsl
suspend infix fun <S, T> Diagonal<S>.map(f: suspend (S)->T): Diagonal<T> = Diagonal(f(first))

/**
 * Map a Diagonal
 */
@MathCatDsl
suspend infix fun <S, T> Diagonal<S>.map(f: suspend CoroutineScope.(S)->T): Diagonal<T> = coroutineScope { Diagonal(f(first)) }