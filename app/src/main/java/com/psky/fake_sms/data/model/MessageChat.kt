package com.psky.fake_sms.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.psky.fake_sms.utils.Define

@Entity(tableName = Define.MessageChat.TABLE_NAME)
data class MessageChat(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = Define.MessageChat.USER_ID)
    var user_id: Int? = null,
    @ColumnInfo(name = Define.MessageChat.CONTENT)
    val content: String,
    @ColumnInfo(name = Define.MessageChat.TIME_STRING)
    val timeString: String,
    @ColumnInfo(name = Define.MessageChat.TIME_LONG)
    val timeLong: Long,
    @ColumnInfo(name = Define.MessageChat.SENDER)
    val sender: Int? = null,
    @ColumnInfo(name = Define.MessageChat.HEADER)
    val header: Int = 0
)