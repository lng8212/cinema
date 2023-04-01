package com.longkd.cinema.utils

import android.util.Patterns
import androidx.annotation.StringRes
import com.longkd.cinema.core.CinemaApplication

object Strings {
    /**
     * This function is for getting the string resources outside of context scope
     */
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return CinemaApplication.instance.getString(stringRes, *formatArgs)
    }
}

/**
 * Returns true if string is valid email pattern
 */
fun String.isValidEmail(): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(this).matches()
}
