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

import org.evoleq.math.cat.marker.MathCatDsl

interface Profunctor<A, B> : ContraFunctor<A>, Functor<B> {
    @MathCatDsl
    fun <C, D> diMap(pre: (C)->A, post: (B)->D): Profunctor<C, D>
    
    @MathCatDsl
    override fun <C> contraMap(f: (C) -> A): Profunctor<C, B> = diMap(f, {b->b})
    
    @MathCatDsl
    override fun <D> map(f: (B) -> D): Profunctor<A, D> = diMap({a->a},f)
}
