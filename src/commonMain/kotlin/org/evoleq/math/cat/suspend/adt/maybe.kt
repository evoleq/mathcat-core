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
package org.evoleq.math.cat.suspend.adt

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.evoleq.math.cat.marker.MathCatDsl


typealias Nothing = Maybe.Nothing

sealed class Maybe<out T> {
    data class Just<T> (val value: T) : Maybe<T>()
    object Nothing : Maybe<kotlin.Nothing>()
    
    companion object {
        /**
         * [Maybe] is a functor
         */
        @MathCatDsl
        suspend operator fun <S, T> invoke(f:suspend CoroutineScope.(S)->T): suspend CoroutineScope.(Maybe<S>) ->Maybe<T> = {
            maybe: Maybe<S> -> when(maybe) {
                is Nothing -> Nothing
                is Just -> Just(f(maybe.value))
            }
        }
    
        /**
         * Return function of [Maybe] monad
         */
        @MathCatDsl
       suspend  fun <T> ret(): suspend CoroutineScope.(T)->Maybe<T> = {t: T ->  Just(t)}
    
        /**
         * Return function of [Maybe] monad
         */
        @MathCatDsl
        suspend fun <T> ret(value : T): Maybe<T> = coroutineScope {  ret<T>()(value) }
    
        /**
         * Multiply [Maybe]s
         */
        @MathCatDsl
        suspend fun <T> multiply(): suspend CoroutineScope.(Maybe<Maybe<T>>)->Maybe<T> = {
            maybe -> when(maybe) {
                is Nothing -> Nothing
                is Just -> maybe.value
            }
        }
    }
}

/**
 * Map a [Maybe]
 */
@MathCatDsl
suspend infix fun <S, T> Maybe<S>.map(f: suspend CoroutineScope.(S)->T): Maybe<T> = coroutineScope { Maybe( f)(this@map) }

/**
 * Bind function of [Maybe]
 */
@MathCatDsl
suspend infix fun <S, T> Maybe<S>.bind(f: suspend CoroutineScope.(S)->Maybe<T>): Maybe<T> = coroutineScope { Maybe.multiply<T>()(Maybe(f)(this@bind)) }

/**
 * Apply function of [Maybe]
 */
@MathCatDsl
suspend fun <S, T> Maybe<suspend CoroutineScope.(S)->T>.apply():suspend CoroutineScope.(Maybe<S>)->Maybe<T> = {
    maybe -> bind { f -> maybe map f }
}

/**
 * Apply function of [Maybe]
 */
@MathCatDsl
suspend infix fun <S, T> Maybe<suspend CoroutineScope.(S)->T>.apply(maybe: Maybe<S>): Maybe<T> = coroutineScope { apply()(maybe) }

/**
 * Discard first
 */
//infix fun <T> Maybe<T>.`*>`(other: Maybe<T>): Maybe<T> =


/**
 * Alternative OR (<|>)
 */
@MathCatDsl
suspend infix fun <T> Maybe<T>.OR(other: Maybe<T>): Maybe<T> = when(this) {
    is Maybe.Just -> this
    is Maybe.Nothing -> when(other) {
        is Maybe.Just -> other
        is Maybe.Nothing -> Maybe.Nothing
    }
}

/**
 * Alternative Empty
 */
@MathCatDsl
@Suppress("FunctionName")
suspend fun Maybe.Companion.Empty() = Maybe.Nothing

@MathCatDsl
suspend fun <T> Maybe<T>.measure(default: T): T = when(this) {
    is Maybe.Just -> value
    is Maybe.Nothing -> default
}