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

interface Product<F1, F2> {
    val factor1: F1
    val factor2: F2
    
    @MathCatDsl
    fun <F3> map1(f: (F1)->F3): Product<F3,F2> = swap<F2, F3>()((Product<F2, F1 ,F3>(f)(swap<F1, F2>()(this))))
    
    @MathCatDsl
    fun <F3> map2(f: (F2)->F3): Product<F1, F3> = Product<F1, F2, F3>(f)(this)
    
    companion object {
        @MathCatDsl
        operator fun <F1, F2, F3> invoke(f: (F2)->F3): (Product<F1, F2>)->Product<F1, F3> = {
            product -> Product(product.factor1, f(product.factor2))
        }
        
        @MathCatDsl
        fun <F1, F2> swap(): (Product<F1, F2>)->Product<F2, F1> = {product -> Product(product.factor2, product.factor1) }
        
        @MathCatDsl
        fun <F1, F2, F3, F4> onPair(f: (Product<F1, F2>)->Product<F3, F4>): (Pair<F1, F2>)->Pair<F3, F4> = {pair: Pair<F1, F2> ->  f(pair.asProduct()).asPair()}
    
        @MathCatDsl
        fun <S1, S2, S3> mergeUnderProduct(): (Either<Product<S1,S3>,Product<S2, S3>>)-> Product<Either<S1, S2>,S3> = {
            either: Either<Product<S1,S3>, Product<S2, S3>> -> when(either) {
                is Left -> Product(Left(either.value.factor1),either.value.factor2)
                is Right -> Product(Right(either.value.factor1),either.value.factor2)
            }
        }
        
        @MathCatDsl
        fun <F1, F2> fromPair(): (Pair<F1, F2>)->Product<F1, F2> = {it.asProduct()}
        
        @MathCatDsl
        fun <F1, F2> toPair(): (Product<F1, F2>)->Pair<F1, F2> = {it.asPair()}
    }
}

/**
 * Constructor function for [Product]
 */
@MathCatDsl
@Suppress("FunctionName")
fun <F1, F2> Product(factor1: F1, factor2: F2): Product<F1, F2> = object : Product<F1, F2> {
    override val factor1: F1 = factor1
    override val factor2: F2 = factor2
}

@MathCatDsl
fun <F1, F2> Pair<F1, F2>.asProduct(): Product<F1, F2> = Product(first,second)

@MathCatDsl
fun <F1, F2> Product<F1, F2>.asPair(): Pair<F1, F2> = Pair(factor1,factor2)