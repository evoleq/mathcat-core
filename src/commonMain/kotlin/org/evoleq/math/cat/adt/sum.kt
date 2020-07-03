package org.evoleq.math.cat.adt

typealias Either<L, R> = Sum<L, R>
typealias Left<L, R> = Sum.Summand1<L,R>
typealias Right<L, R> = Sum.Summand2<L,R>

typealias Maybe<T> = Sum<T, kotlin.Nothing>
typealias Just<T> = Sum.Summand1<T,kotlin.Nothing>
typealias Nothing<T> = Sum.Summand2<T, kotlin.Nothing>


sealed class Sum<out S1, out S2> {
    data class Summand1<S1, S2>(val value: S1): Sum<S1, S2>()
    data class Summand2<S1, S2>(val value: S2): Sum<S1, S2>()
}