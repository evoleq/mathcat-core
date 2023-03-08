package org.evoleq.math.cat.aux

import org.evoleq.math.cat.marker.MathCatDsl

@MathCatDsl
infix fun <S, T> ((S)-> T).`$`(s: S): T = this(s)

@MathCatDsl
infix fun <R, S, T> ((S)->T).o(other: (R)->S): (R)->T = {r -> this(other(r)) }

@MathCatDsl
infix fun <S, T> List<S>.map(f: (S)->T): List<T> = mapRec(arrayListOf(),f)

tailrec fun <S, T> List<S>.mapRec(target: ArrayList<T>, f: (S)->T): List<T> = when(isEmpty()){
    true -> target
    false -> {
        target.add(f(first()))
        drop(1).mapRec(target,f)
    }
}