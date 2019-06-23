package com.psky.fake_sms.adapter.show

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.psky.fake_sms.R
import com.psky.fake_sms.data.model.MessageChat
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.Define
import com.psky.fake_sms.utils.Utils
import kotlinx.android.synthetic.main.header_message_chat_line.view.*
import kotlinx.android.synthetic.main.message_chat_me_line.view.*
import kotlinx.android.synthetic.main.message_chat_friend_messenger_line.view.*
import java.io.File

class LineChatAdapter(var items: MutableList<MessageChat>, val context: Context?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM_ME = 1
    private val TYPE_ITEM_FRIEND = 2

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            TYPE_HEADER -> {
                view = LayoutInflater.from(context).inflate(R.layout.header_message_chat_line, p0, false)
                HeaderViewHolder(view)
            }
            TYPE_ITEM_ME -> {
                view = LayoutInflater.from(context).inflate(R.layout.message_chat_me_line, p0, false)
                MessageMeViewHolder(view)
            }
            TYPE_ITEM_FRIEND -> {
                view = LayoutInflater.from(context).inflate(R.layout.message_chat_friend_messenger_line, p0, false)
                MessageFriendViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(R.layout.header_message_chat_line, p0, false)
                HeaderViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (isPositionHeader(position)) {
            return TYPE_HEADER
        }
        if (items[position].sender == Define.MessageChat.SENDER_FRIEND) {
            return TYPE_ITEM_FRIEND
        }
        return TYPE_ITEM_ME
    }

    /**
     * Used to check if the object is a header
     * return true if header = 1 else false
     */
    private fun isPositionHeader(position: Int): Boolean {
        return items[position].header == 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.title.text = items[position].content

            is MessageFriendViewHolder -> {
                holder.messageFriend.text = items[position].content

                Glide.with(holder.logoFriends)
                    .load(File(DataUtils.shared.user?.image))
                    .into(holder.logoFriends)

                holder.nameUser.text = DataUtils.shared.user?.name
                holder.time.text = "${Utils.shared.formatDateToStringShowMessenger(items[position].timeLong)}"
            }
            is MessageMeViewHolder -> {
                holder.time.text = "Read ${Utils.shared.formatDateToStringShowMessenger(items[position].timeLong)}"
                holder.messageMe.text = items[position].content
            }
        }

    }


    /**
     * Class ViewHolder of Me
     */
    class MessageMeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageMe: TextView = view.messageMe
        val time: TextView = view.timeMessagesMe
    }

    /**
     * Class ViewHolder of Friend
     */
    class MessageFriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageFriend: TextView = view.messageFriend
        val logoFriends = view.logoFriends
        val nameUser = view.nameUser
        val time = view.timeMessages
    }

    /**
     * Class ViewHolder of Header
     */
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.header
    }
}