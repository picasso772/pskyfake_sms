package com.psky.fake_sms.utils

import com.psky.fake_sms.service.SingletonService

class DataUtils {
    companion object {
        val shared = SingletonService.get(DataUtils::class)
    }

    var backID: Int = 0
}