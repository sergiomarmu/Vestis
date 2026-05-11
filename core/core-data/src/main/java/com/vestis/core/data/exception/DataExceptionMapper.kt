package com.vestis.core.data.exception

import com.vestis.core.domain.DomainException

fun DataException.toExceptionDomain() = when (this) {
    is DataException.Network.Unreachable -> DomainException.Network.NoConnection(
        this.message
    )

    is DataException.Network.Unparseable -> DomainException.Network.InvalidResponse(
        this.message
    )

    is DataException.Network.Unexpected -> DomainException.Network.UnknownError(
        this.message
    )

    is DataException.Unexpected -> DomainException.Unexpected(
        this.message
    )
}