package com.psky.fake_sms.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.psky.fake_sms.data.model.MessageChat

@Dao
interface MessageDao {
    /**
     * Perform the insert function
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messages: MutableList<MessageChat>)

    /**
     * Performs the function of retrieving a user record
     */
    @Query("SELECT * FROM tb_message")
    fun getMessages(): List<MessageChat>

    /**
     * Performs the function of retrieving a user record
     */
    @Query("SELECT * FROM tb_message WHERE user_id == :userId")
    fun getMessagesByUser(userId: Int): List<MessageChat>

    /**
     *
     */
    @Query("DELETE  FROM tb_message WHERE user_id == :userId")
    fun deleteMessagesByUser(userId: Int)
}