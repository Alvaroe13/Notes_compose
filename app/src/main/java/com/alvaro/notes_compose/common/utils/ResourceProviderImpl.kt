package com.alvaro.notes_compose.common.utils

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(
    private val context: Context
) : ResourceProvider {
    override fun getString(@StringRes id: Int, vararg args: Any) = context.getString(id, *args)
    override fun getQuantityString(@PluralsRes id: Int, quantity: Int) = context.resources.getQuantityString(id, quantity)
    override fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any) = context.resources.getQuantityString(id, quantity, *formatArgs)
}