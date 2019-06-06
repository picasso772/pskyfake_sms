package com.psky.fake_sms.utils

import com.psky.fake_sms.entity.NotificationError
import com.psky.fake_sms.service.SingletonService

/**
 * Create by AnhPQ in 31/05/2019
 */
class DataUtils {
    companion object {
        val shared = SingletonService.get(DataUtils::class)
    }

    var backID: Int = 0

    var notificationErrorID: NotificationError = NotificationError.none
}