package com.vestis.core.domain

sealed class DomainException: Exception() {
    abstract override val message: String?

    sealed class Network : DomainException() {
        data class NoConnection(
            override val message: String? = null
        ) : Network()

        data class InvalidResponse(
            override val message: String? = null
        ) : Network()

        data class UnknownError(
            override val message: String? = null
        ) : Network()
    }

    data class Unexpected(
        override val message: String? = null
    ) : DomainException()
}