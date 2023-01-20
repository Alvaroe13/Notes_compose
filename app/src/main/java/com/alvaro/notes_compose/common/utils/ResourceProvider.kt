package com.alvaro.notes_compose.common.utils

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(@StringRes id: Int, vararg args: Any): String
    fun getQuantityString(@PluralsRes id: Int, quantity: Int): String
    fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String
}