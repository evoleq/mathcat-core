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
package org.evoleq.math.cat.suspended.applicative

import kotlinx.coroutines.CoroutineScope
import org.evoleq.math.cat.marker.MathCatDsl
import org.evoleq.math.cat.suspended.functor.ScopedSuspendedFunctor

interface ScopedSuspendedApplicative<T> : ScopedSuspendedFunctor<T> {
    
    @MathCatDsl
    override suspend fun <U> map(f: suspend CoroutineScope.(T) -> U): ScopedSuspendedApplicative<U>
    
    @MathCatDsl
    suspend fun <U> apply(applicative: ScopedSuspendedApplicative<suspend CoroutineScope.(T)->U>): ScopedSuspendedApplicative<U>
}

@MathCatDsl
suspend fun <S, T> ScopedSuspendedApplicative<suspend CoroutineScope.(S)->T>.pipe(next: ScopedSuspendedApplicative<S>): ScopedSuspendedApplicative<T> =
    next.apply(this)