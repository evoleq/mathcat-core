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

interface ScopedSuspendedProfunctor<A, B> : ScopedSuspendedContraFunctor<A>, ScopedSuspendedFunctor<B> {
    
    @MathCatDsl
    suspend fun <C, D> diMap(pre: suspend CoroutineScope.(C)->A, post: suspend CoroutineScope.(B)->D): ScopedSuspendedProfunctor<C, D>
    
    @MathCatDsl
    override suspend fun <C> contraMap(f: suspend CoroutineScope.(C) -> A): ScopedSuspendedProfunctor<C, B> = diMap(f, {b->b})
    
    @MathCatDsl
    override suspend fun <D> map(f: suspend CoroutineScope.(B) -> D): ScopedSuspendedProfunctor<A, D> = diMap({a->a}, f)
}