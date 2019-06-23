package com.psky.fake_sms.utils

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.reflect.KClass

fun <E> MutableList<E>.append(element: E) {
    this.add(element)
}

fun <E> MutableList<E>.append(element: List<E>) {
    element.forEach { this.add(it) }
}

fun <E> MutableList<E>.removeAll() {

}

fun <T : Fragment> T.getKClass(): KClass<T> {
    return javaClass.kotlin
}

/**
 * Extension View
 */
var View.isHidden: Boolean
    get() = visibility == View.INVISIBLE || visibility == View.GONE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE

    }

fun View.clearFocusAndHideKeyboard() {
    this.clearFocus()
    hideKeyboard()
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Used to convert positive float into negative float
 */
val Float.negative: Float
    get() = -1F * this