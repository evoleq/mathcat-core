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
package org.evoleq.math.cat.comonad

import org.evoleq.math.cat.functor.Functor
import org.evoleq.math.cat.marker.MathCatDsl

interface Comonad<out T> : Functor<T> {
    
    @MathCatDsl
    fun extract(): T
    
    @MathCatDsl
    fun duplicate(): Comonad<Comonad<T>>
}

interface CoKleisliArrow<S, T> : Functor<T>