package com.psky.fake_sms.custom

import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.psky.fake_sms.R
import com.psky.fake_sms.service.ScreenService

object AppColor {
    val dialogBackground = get(R.color.dialogBackground)

    private fun get(colorId: Int): UIColor {
        val context: AppCompatActivity = ScreenService.getActivity() ?: return UIColor.black
        return UIColor(ContextCompat.getColor(context, colorId))
    }
}