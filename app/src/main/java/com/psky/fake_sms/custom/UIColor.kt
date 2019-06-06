package com.psky.fake_sms.custom

import android.graphics.Color

class UIColor {

    companion object {
        val white = UIColor(rgbValue = 0xFFFFFF)
        val black = UIColor(rgbValue = 0x000000)
        val red = UIColor(rgbValue = 0xFF0000)
        val green = UIColor(rgbValue = 0x00FF00)
        val blue = UIColor(rgbValue = 0x0000FF)
        val clear = UIColor(rgbValue = 0xFFFFFF, alpha = 0.0)
        val orange = UIColor(rgbValue = 0xFFA500)
        val gray = UIColor(rgbValue = 0x808080)
        val yellow = UIColor(rgbValue = 0xFFFF00)
        val cyan = UIColor(rgbValue = 0x00FFFF)
        val lightGray = UIColor(rgbValue = 0xFFFFFF, alpha = 0.667)
        val defaultTint = UIColor(rgbValue = 0x007AF)
    }

    private var argbInt = 0xFFFFFFFF.toInt()

    val intValue: Int get() = argbInt

    val colorCode: String? get() = String.format("%06X", 0xFFFFFF and intValue)

    constructor(argbInt: Int) {
        this.argbInt = argbInt
    }

    constructor(rgbValue: Int, alpha: Double = 1.0) {
        setColorInt(rgbValue, alpha)
    }

    constructor(rgbValue: Long, alpha: Double = 1.0) : this(rgbValue.toInt(), alpha)

    constructor(rgbHex: String, alpha: Double = 1.0) {
        val code = rgbHex.removePrefix("#")
        if (code.count() != 6) {
            return
        }
        val rgbValue = code.toIntOrNull(16) ?: 0xFFFFFF
        setColorInt(rgbValue, alpha)
    }

    constructor(argbHex: String) {
        val code = argbHex.removePrefix("#")
        if (code.count() != 8) {
            return
        }

        val aStr = code.substring(0..1)
        val alpha = (aStr.toIntOrNull(16) ?: 0) / 255.0
        val rgb = code.substring(startIndex = 2)

        val rgbValue = rgb.toIntOrNull(16) ?: 0xFFFFFF
        setColorInt(rgbValue, alpha)
    }

    constructor(white: Double = 1.0, alpha: Double = 1.0) {
        val iWhite = to255(white)
        argbInt = Color.argb(to255(alpha), iWhite, iWhite, iWhite)
    }

    constructor(red: Double, green: Double, blue: Double, alpha: Double) {
        argbInt = Color.argb(to255(alpha), to255(red), to255(green), to255(blue))
    }

    private fun setColorInt(rgbValue: Int, alpha: Double) {
        var iAlpha = to255(alpha)   // 0x 00 00 00 FF
        iAlpha = iAlpha shl (8 * 3) // 0x FF 00 00 00
        argbInt = iAlpha or rgbValue
    }

    private fun to255(d: Double): Int {
        return (d * 255.0).toInt()
    }

    override fun equals(other: Any?): Boolean {
        val rhs = (other as? UIColor) ?: return false
        return this.intValue == rhs.intValue
    }

    override fun hashCode(): Int {
        return argbInt
    }
}
