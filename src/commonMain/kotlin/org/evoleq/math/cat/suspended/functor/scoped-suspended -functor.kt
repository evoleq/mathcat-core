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
package org.evoleq.math.cat.suspended.functor

import kotlinx.coroutines.CoroutineScope
import org.evoleq.math.cat.marker.MathCatDsl

interface ScopedSuspendedFunctor<out T> {
    
    @MathCatDsl
    suspend infix fun <U> map(f: suspend CoroutineScope.(T)->U): ScopedSuspendedFunctor<U>
}

interface ScopedSuspendedContraFunctor<in T> {
    @MathCatDsl
    suspend infix fun <S> contraMap(f: suspend CoroutineScope.(S)->T): ScopedSuspendedContraFunctor<S>
}