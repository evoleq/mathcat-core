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

import kotlinx.coroutines.CoroutineScope
import org.evoleq.math.cat.marker.MathCatDsl

sealed class Sum<out S1, out S2> {
    data class Summand1<S1, S2>(val value: S1): Sum<S1, S2>()
    data class Summand2<S1, S2>(val value: S2): Sum<S1, S2>()
    
    companion object {
    
        @MathCatDsl
        fun <S1, S2> iota1(): (S1)->Sum<S1, S2> = {s1 -> Summand1(s1)}
    
        @MathCatDsl
        fun <S1, S2> iota2(): (S2)->Sum<S1, S2> = {s2 -> Summand2(s2)}
    
        @MathCatDsl
        operator fun <S1, S2, S3> invoke(f: (S2)->S3): (Sum<S1, S2>)->Sum<S1, S3> = lift2(f)
    
        @MathCatDsl
        operator fun <S1, S2, S3> invoke(f: suspend CoroutineScope.(S2)->S3): suspend CoroutineScope.(Sum<S1, S2>)->Sum<S1, S3> = {
            sum -> when(sum) {
                is Summand1 -> Summand1(sum.value)
                is Summand2 -> Summand2(f(sum.value))
            }
        }
    
        @MathCatDsl
        fun <S1, S2, S3> lift1(f: (S1)->S3): (Sum<S1, S2>)->Sum<S3, S2> = {sum -> sum map1 f}
    
        @MathCatDsl
        fun <S1, S2, S3> lift2(f: (S2)->S3): (Sum<S1, S2>)->Sum<S1, S3> = {sum -> sum map2 f}
        
        @MathCatDsl
        fun <S1, S2> ret(): (S2)-> Sum<S1,S2> = iota2()

        @MathCatDsl
        fun <S1, S2> multiply(): (Sum<Sum<S1, S2>, S2>)->Sum<S1, S2> = {sum ->
            when(sum) {
                is Summand1 -> sum.value
                is Summand2 -> Summand2(sum.value)
            }
        }

        @MathCatDsl
        fun <S1, S2> multiply1(): (Sum<S1, Sum<S1, S2>>)->Sum<S1, S2> = {sum ->
            when(sum) {
                is Summand2 -> sum.value
                is Summand1 -> Summand1(sum.value)
            }
        }

        @MathCatDsl
        fun <S1, S2, S3> merge() :(Sum<Sum<S1, S2>, Sum<S1,S3>>)->Sum<S1,Sum<S2,S3>> = {
            sum -> when(sum) {
                is Summand1 -> when(val summand = sum.value) {
                    is Summand1 -> Summand1(summand.value)
                    is Summand2 -> Summand2(Summand1(summand.value))
                }
                is Summand2 -> when(val summand = sum.value) {
                    is Summand1 -> Summand1(summand.value)
                    is Summand2 -> Summand2(Summand2(summand.value))
                }
            }
        }
    
        @MathCatDsl
        fun <S1, S2, S3> swapOverPair(): (Pair<Either<S1, S2>,S3>)-> Either<S1,Pair<S2, S3>> = {
            pair: Pair<Either<S1, S2>, S3> -> when(val either = pair.first) {
                is Summand1 -> Summand1(either.value)
                is Summand2 -> Summand2(Pair(either.value,pair.second))
            }
        }

        @MathCatDsl
        fun <S1, S2, T> measureBy(measure: (Sum<S1, S2>)->T): (Sum<S1, S2>)->T = measure
    }
}


@MathCatDsl
infix fun <S1, S2, S3> Sum<S1, S2>.map1(f: (S1)->S3): Sum<S3, S2> = when(this) {
    is Sum.Summand1 -> Sum.Summand1(f(value))
    is Sum.Summand2 -> Sum.Summand2(value)
}

@MathCatDsl
infix fun <S1, S2, S3> Sum<S1, S2>.map2(f: (S2)->S3): Sum<S1, S3> = when(this) {
    is Sum.Summand1 -> Sum.Summand1(value)
    is Sum.Summand2 -> Sum.Summand2(f(value))
}

@MathCatDsl
fun <S1, S2> Sum<S1, S2>.swap(): Sum<S2, S1> = when(this) {
    is Sum.Summand1 -> Sum.Summand2(value)
    is Sum.Summand2 -> Sum.Summand1(value)
}

@MathCatDsl
fun <S1, S2> Sum.Companion.swap():(Sum<S1, S2>)-> Sum<S2, S1> = {sum ->sum.swap()}

@MathCatDsl
fun <S1, S2, S3> Sum<Sum<S1, S2>,S3>.assocTail(): Sum<S1,Sum< S2,S3>> = when(val summand = this) {
    is Sum.Summand1 -> when(summand.value) {
        is Sum.Summand1 -> Sum.Summand1(summand.value.value)
        is Sum.Summand2 -> Sum.Summand2(Sum.Summand1(summand.value.value))
    }
    is Sum.Summand2 -> Sum.Summand2(Sum.Summand2(summand.value))
}

@MathCatDsl
fun <S1, S2, S3> Sum.Companion.assocTail(): (Sum<Sum<S1, S2>,S3>)-> Sum<S1,Sum< S2,S3>> = {e -> e.assocTail()}

@MathCatDsl
fun <S1, S2, S3> Sum<S1,Sum< S2,S3>>.assocHead():Sum<Sum<S1, S2>,S3> = when(val summand = this) {
    is Sum.Summand1 -> Sum.Summand1(Sum.Summand1(summand.value))
    is Sum.Summand2 -> when(summand.value) {
        is Sum.Summand1 -> Sum.Summand1(Sum.Summand2(summand.value.value))
        is Sum.Summand2 -> Sum.Summand2(summand.value.value)
    }
}

@MathCatDsl
fun <S1, S2> Sum<S1, Sum<S1, S2>>.multiply1(): Sum<S1, S2> = Sum.multiply1<S1, S2>()(this)

@MathCatDsl
infix fun <S1, S2, T> Sum<S1, S2>.measureBy(measure: (Sum<S1, S2>)->T): (Sum<S1, S2>)->T = Sum.measureBy(measure)