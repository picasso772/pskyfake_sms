package com.psky.fake_sms.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.psky.fake_sms.utils.Define

@Entity(tableName = Define.User.TABLE_NAME)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = Define.User.NAME)
    val name: String,
    @ColumnInfo(name = Define.User.PHONE)
    val phone: String,
    @ColumnInfo(name = Define.User.IMAGE)
    val image: String,
    @ColumnInfo(name = Define.User.TIME_MESSAGES)
    var timeMessages: Long? = null,
    @ColumnInfo(name = Define.User.MESSAGE_LAST)
    var messageLast: String? = null
)