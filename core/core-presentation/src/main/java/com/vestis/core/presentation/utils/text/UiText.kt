package com.vestis.core.presentation.utils.text

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vestis.core.domain.DomainException
import com.vestis.core.presentation.R
import com.vestis.core.presentation.mapper.toUiMessageRes

sealed interface UiText {
    data class DynamicString(
        val value: String
    ) : UiText

    data class StringResource(
        @StringRes val resId: Int
    ) : UiText

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> stringResource(id = resId)
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> context.getString(resId)
    }
}

fun Throwable.asUiText(): UiText = when (this) {
    is DomainException -> {
        UiText.StringResource(resId = this.toUiMessageRes())
    }

    else -> {
        this.message
            ?.let { UiText.DynamicString(value = it) }
            ?: UiText.StringResource(resId = R.string.core_unknown_error)
    }
}