package org.evoleq.math.cat.adt

import org.evoleq.math.cat.marker.MathCatDsl


typealias Either<L, R> = Sum<L, R>
typealias Left<L, R> = Sum.Summand1<L,R>
typealias Right<L, R> = Sum.Summand2<L,R>

@MathCatDsl
infix fun <L,K , R> Either<L, R>.mapLeft(f: (L)->K): Either<K, R> = map1(f) as Either<K, R>