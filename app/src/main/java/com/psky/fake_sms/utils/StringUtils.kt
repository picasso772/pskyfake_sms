package com.psky.fake_sms.utils

import java.lang.StringBuilder

/**
 * Get layoutName
 */
fun String.toSnakeCase(): String {
    val currentValue = this
    var value = StringBuilder()
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