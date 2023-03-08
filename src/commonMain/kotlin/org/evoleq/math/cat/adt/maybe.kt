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
package org.evoleq.math.cat.adt

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
        operator fun <S, T> invoke(f:(S)->T): (Maybe<S>) ->Maybe<T> = {
            maybe: Maybe<S> -> when(maybe) {
                is Nothing -> Nothing
                is Just -> Just(f(maybe.value))
            }
        }
    
        /**
         * Return function of [Maybe] monad
         */
        @MathCatDsl
        fun <T> ret(): (T)->Maybe<T> = {t: T ->  Just(t)}
    
        /**
         * Return function of [Maybe] monad
         */
        @MathCatDsl
        fun <T> ret(value : T): Maybe<T> = ret<T>()(value)
    
        /**
         * Multiply [Maybe]s
         */
        @MathCatDsl
        fun <T> multiply(): (Maybe<Maybe<T>>)->Maybe<T> = {
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
infix fun <S, T> Maybe<S>.map(f: (S)->T): Maybe<T> = Maybe(f)(this)

/**
 * Bind function of [Maybe]
 */
@MathCatDsl
infix fun <S, T> Maybe<S>.bind(f: (S)->Maybe<T>): Maybe<T> = Maybe.multiply<T>()(Maybe(f)(this))

/**
 * Apply function of [Maybe]
 */
@MathCatDsl
fun <S, T> Maybe<(S)->T>.apply(): (Maybe<S>)->Maybe<T> = {
    maybe -> bind { f -> maybe map f }
}

/**
 * Apply function of [Maybe]
 */
@MathCatDsl
fun <S, T> Maybe<(S)->T>.apply(maybe: Maybe<S>): Maybe<T> = apply()(maybe)

/**
 * Alternative OR (<|>)
 */
@MathCatDsl
@Suppress("FunctionName")
infix fun <T> Maybe<T>.OR(other: Maybe<T>): Maybe<T> = when(this) {
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
fun Maybe.Companion.Empty() = Maybe.Nothing

infix fun <S, T> Maybe<S>.discardLeft(right: Maybe<T>): Maybe<T> = this bind { right }


infix fun <S, T> Maybe<S>.discardRight(right: Maybe<T>): Maybe<S> = right discardLeft this


@MathCatDsl
fun <T> Maybe<T>.measure(default: T): T = when(this) {
    is Maybe.Just -> value
    is Maybe.Nothing -> default
}

@MathCatDsl
infix fun <S, T> Maybe<S>.measureBy(measure: (Maybe<S>)->T): T = measure(this)