package com.psky.fake_sms.utils

import android.content.Context
import java.lang.StringBuilder

/**
 * Get layoutName
 */
fun String.toSnakeCase(): String {
    val currentValue = this
    val value = StringBuilder()
    for (x in 0 until currentValue.length) {
        val chart = currentValue[x]
        if (chart.isLowerCase()) {
            value.append(chart)
        }
        if (chart.isUpperCase() && x == 0) {
            value.append(chart.toLowerCase())
        }
        if (chart.isUpperCase() && x != 0) {
            value.append("_" + chart.toLowerCase())
        }
    }
    return value.toString()
}

/**
 *
 */
fun String.localizedString(context: Context?): String {
    val context = context ?: return "Context is null"
    val stringID = context.resources.getIdentifier(this, "string", context.packageName)
    if (stringID == 0) {
        return this
    }
    return context.resources.getString(stringID)
}

/**
 *
 */
val String.toInt: Int
    get() {
        for (it: Char in this) {
            if (it < '0' || it > '9') return 0
        }
        return this.toInt()
    }