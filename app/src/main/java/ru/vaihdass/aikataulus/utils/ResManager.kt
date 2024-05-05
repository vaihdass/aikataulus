package ru.vaihdass.aikataulus.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import javax.inject.Inject

class ResManager @Inject constructor(private val ctx: Context) {

    fun getString(@StringRes res: Int): String = ctx.resources.getString(res)

    fun getString(@StringRes res: Int, vararg args: Any?): String {
        return ctx.resources.getString(res, *args)
    }

    fun getColor(@ColorRes res: Int): Int = ContextCompat.getColor(ctx, res)
}