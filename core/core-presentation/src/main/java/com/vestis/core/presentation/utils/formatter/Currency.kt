package com.vestis.core.presentation.utils.formatter

import java.text.NumberFormat
import java.util.Locale

fun Float.toFormattedPrice() = NumberFormat
    .getCurrencyInstance(Locale.getDefault())
    .format(this)