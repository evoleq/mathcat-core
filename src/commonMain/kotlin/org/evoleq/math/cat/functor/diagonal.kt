/**
 * Copyright (c) 2020 Dr. Florian Schmidt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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