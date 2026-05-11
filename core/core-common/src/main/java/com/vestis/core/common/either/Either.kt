package com.vestis.core.common.either

sealed class Either<out L, out R> {
    data class Left<out L>(
        val value: L
    ) : Either<L, Nothing>()

    data class Right<out R>(
        val value: R
    ) : Either<Nothing, R>()

    inline fun <nL, nR> map(
        ifLeft: (L) -> nL,
        ifRight: (R) -> nR
    ): Either<nL, nR> = when (this) {
        is Left -> Left(ifLeft(this.value))
        is Right -> Right(ifRight(this.value))
    }

    inline fun <T> fold(
        ifLeft: (L) -> T,
        ifRight: (R) -> T
    ): T = when (this) {
        is Left -> ifLeft(this.value)
        is Right -> ifRight(this.value)
    }

    inline fun <L, R, nR> Either<L, R>.mapSuccess(
        transform: (R) -> nR
    ): Either<L, nR> = when (this) {
        is Left -> this
        is Right -> Right(transform(this.value))
    }
}