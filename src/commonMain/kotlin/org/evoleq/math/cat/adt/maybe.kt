package org.evoleq.math.cat.adt

typealias Nothing = Maybe.Nothing

sealed class Maybe<out T> {
    data class Just<T> (val value: T) : Maybe<T>()
    object Nothing : Maybe<kotlin.Nothing>()
}