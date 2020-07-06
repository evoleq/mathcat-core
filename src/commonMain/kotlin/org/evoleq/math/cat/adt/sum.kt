package org.evoleq.math.cat.adt

import org.evoleq.math.cat.marker.MathCatDsl

typealias Either<L, R> = Sum<L, R>
typealias Left<L, R> = Sum.Summand1<L,R>
typealias Right<L, R> = Sum.Summand2<L,R>

typealias Maybe<T> = Sum<kotlin.Nothing, T>
typealias Just<T> = Sum.Summand1<kotlin.Nothing, T>
typealias Nothing<T> = Sum.Summand2<kotlin.Nothing, T>


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
        fun <S1, S2, S3> lift1(f: (S1)->S3): (Sum<S1, S2>)->Sum<S3, S2> = {sum -> sum map1 f}
    
        @MathCatDsl
        fun <S1, S2, S3> lift2(f: (S2)->S3): (Sum<S1, S2>)->Sum<S1, S3> = {sum -> sum map2 f}
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
fun <S1, S2> swap():(Sum<S1, S2>)-> Sum<S2, S1> = {sum ->sum.swap()}

@MathCatDsl
fun <S1, S2, S3> Sum<Sum<S1, S2>,S3>.assocTail(): Sum<S1,Sum< S2,S3>> = when(val summand = this) {
    is Sum.Summand1 -> when(summand.value) {
        is Sum.Summand1 -> Sum.Summand1(summand.value.value)
        is Sum.Summand2 -> Sum.Summand2(Sum.Summand1(summand.value.value))
    }
    is Sum.Summand2 -> Sum.Summand2(Sum.Summand2(summand.value))
}

@MathCatDsl
fun <S1, S2, S3> assocTail(): (Sum<Sum<S1, S2>,S3>)-> Sum<S1,Sum< S2,S3>> = {e -> e.assocTail()}

@MathCatDsl
fun <S1, S2, S3> Sum<S1,Sum< S2,S3>>.assocHead():Sum<Sum<S1, S2>,S3> = when(val summand = this) {
    is Sum.Summand1 -> Sum.Summand1(Sum.Summand1(summand.value))
    is Sum.Summand2 -> when(summand.value) {
        is Sum.Summand1 -> Sum.Summand1(Sum.Summand2(summand.value.value))
        is Sum.Summand2 -> Sum.Summand2(summand.value.value)
    }
}