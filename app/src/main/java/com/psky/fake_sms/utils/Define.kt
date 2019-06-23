package com.psky.fake_sms.utils

import android.os.Environment
import java.io.File

class Define {

    /**
     * User table information fields
     */
    class User {
        companion object {
            const val TABLE_NAME: String = "tb_user"
            const val NAME: String = "userName"
            const val PHONE: String = "userPhone"
            const val IMAGE: String = "userImage"
            const val TIME_MESSAGES: String = "messageTimeMessages"
            const val MESSAGE_LAST: String = "messageLast"
        }
    }

    /**
     * Message chat table information fields
     */
    class MessageChat {
        companion object {
            const val TABLE_NAME: String = "tb_message"
            const val USER_ID: String = "user_id"
            const val CONTENT: String = "messageContent"
            const val TIME_STRING: String = "messageTimeString"
            const val TIME_LONG: String = "messageTimeLong"
            const val SENDER: String = "messageSender"
            const val HEADER: String = "messageHeader"

            const val SENDER_FRIEND = 0
            const val SENDER_ME = 1
        }
    }

    class Fields {
        companion object {
            var ROOT_FOLDER = Environment.getExternalStorageDirectory().toString() + File.separator + "fake_sms"
            const val FORMAT_IMAGE = ".jpg"
            const val TIME_MAX = 1 * 60 * 1000L
            const val REQUEST_LIBRARY = 201
            const val REQUEST_CAMERA = 301
            const val PREFS_FILENAME = "fake_text"

            const val STATUS_RATE = "statusRate"

            const val STATUS_RATE_DEFAULT = "none"
            const val STATUS_RATE_LATER = "rate_later"
            const val STATUS_RATE_NO = "rate_no"
            const val STATUS_RATE_YES = "rate_yes"

            const val RATE_LATER_NEXT = "rate_later_next"
            const val RATE_NO_NEXT = "rate_no_next"
            const val RATE_YES_NEXT = "rate_yes_next"
        }
    }
}