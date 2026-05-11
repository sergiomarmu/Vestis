package com.vestis.core.presentation.mapper

import com.vestis.core.domain.DomainException


fun DomainException.toUiMessage(): String = when (this) {
    is DomainException.Network.NoConnection -> "No internet connection"
    is DomainException.Network.InvalidResponse -> "Something went wrong, try again"
    is DomainException.Network.UnknownError -> "Something went wrong, try again"
    is DomainException.Unexpected -> "Something went wrong"
}