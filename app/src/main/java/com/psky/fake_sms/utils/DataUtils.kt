package com.psky.fake_sms.utils

import com.psky.fake_sms.data.model.MessageChat
import com.psky.fake_sms.data.model.User
import com.psky.fake_sms.entity.NotificationError
import com.psky.fake_sms.screen.chat.StatusSelect
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

    var user: User? = null

    var users: List<User>? = null

    var messages: MutableList<MessageChat>? = null

    var pathImage: String? = null

    /**
     * new
     */
    var name: String? = null
    var phone: String? = null

    fun clearDataTemp() {
        name = null
        phone = null
    }

    /**
     * Used to reset data : user and messages
     */
    fun resetData() {
        user = null
        messages = null
    }
}