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
package org.evoleq.math.cat.suspended.monad

import kotlinx.coroutines.CoroutineScope
import org.evoleq.math.cat.marker.MathCatDsl
import org.evoleq.math.cat.suspended.applicative.ScopedSuspendedApplicative

abstract class ScopedSuspendedMonad<T>(protected val value: T): ScopedSuspendedApplicative<T> {
    
    @MathCatDsl
    abstract suspend infix
    fun <U> bind(f: suspend CoroutineScope.(T)->ScopedSuspendedMonad<U>): ScopedSuspendedMonad<U>
    
    @MathCatDsl
    abstract override suspend infix
    fun <U> map(f: suspend CoroutineScope.(T) -> U): ScopedSuspendedMonad<U>
    
    @MathCatDsl
    override suspend fun <U> apply(
        applicative: ScopedSuspendedApplicative<suspend CoroutineScope.(T) -> U>
    ): ScopedSuspendedMonad<U> = (applicative as ScopedSuspendedMonad<suspend CoroutineScope.(T)->U>) bind {
        g -> this@ScopedSuspendedMonad map g
    }
}

@MathCatDsl
suspend fun <T> ScopedSuspendedMonad<ScopedSuspendedMonad<T>>.multiply(): ScopedSuspendedMonad<T> = bind{m -> m}