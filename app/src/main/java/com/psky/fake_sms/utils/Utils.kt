package com.psky.fake_sms.utils

import com.psky.fake_sms.service.SingletonService

/**
 * Create by AnhPQ in 04/05/2019
 */
class Utils {
    companion object {
        val shared = SingletonService.get(Utils::class)
    }

    fun formatDuration(time: Long): String {
        val time : Long = Math.round((time / 1000L).toDouble())
        if (time > 99 || time < 0) {
            return "00:00:00"
        }
        if (time >= 60) {
            return "00:01:${time - 60}"
        }
        if (time < 10) {
            return "00:00:0$time"
        }
        return "00:00:$time"
    }
}