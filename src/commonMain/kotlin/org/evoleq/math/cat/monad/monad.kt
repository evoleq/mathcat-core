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
package org.evoleq.math.cat.monad

import org.evoleq.math.cat.applicative.Applicative
import org.evoleq.math.cat.functor.ContraFunctor
import org.evoleq.math.cat.marker.MathCatDsl


interface Monad<out T> : Applicative<T> {
    @MathCatDsl
    override  infix fun <U> map(f: (T)->U): Monad<U>
    
    @MathCatDsl
    override infix fun <U> apply(applicative: Applicative<(T) -> U>): Monad<U> = (applicative as Monad<(T)->U>) bind {
        g -> this@Monad map g
    }
    
    @MathCatDsl
    infix fun <U> bind(f: (T)->Monad<U>): Monad<U>
}

@MathCatDsl
fun <T> Monad<Monad<T>>.multiply(): Monad<T> = bind{ m -> m }

interface KleisliArrow<in S, out T> : (S)->Monad<T>, Applicative<T>, ContraFunctor<S> {
    
    @MathCatDsl
    operator fun <U> times(other: KleisliArrow<T, U>): KleisliArrow<S, U> = KleisliArrow{
        s -> this@KleisliArrow(s) bind other
    }
    
    @MathCatDsl
    override infix fun <U> map(f: (T) -> U): KleisliArrow<S, U> = KleisliArrow{
        s -> this@KleisliArrow(s) map f
    }
    
    @MathCatDsl
    override infix fun <U> apply(applicative: Applicative<(T) -> U>): KleisliArrow<S, U> = KleisliArrow {
        s -> this@KleisliArrow(s) apply applicative
    }
    
    @MathCatDsl
    override fun <B> contraMap(f: (B) -> S): KleisliArrow<B, T> = KleisliArrow {
        b->this(f(b))
    }
}

@MathCatDsl
@Suppress("FunctionName")
fun <S, T> KleisliArrow(arrow: (S)->Monad<T>): KleisliArrow<S, T> = object : KleisliArrow<S, T> {
    override fun invoke(p1: S): Monad<T> = arrow(p1)
}
