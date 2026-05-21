package com.vestis.core.presentation.mapper

import androidx.annotation.StringRes
import com.vestis.core.domain.DomainException
import com.vestis.core.presentation.R

@StringRes
fun DomainException.toUiMessageRes(): Int = when (this) {
    is DomainException.Network.NoConnection -> R.string.core_error_no_internet
    is DomainException.Network.InvalidResponse,
    is DomainException.Network.UnknownError -> R.string.core_error_invalid_response

    is DomainException.Unexpected -> R.string.core_error_unexpected
}