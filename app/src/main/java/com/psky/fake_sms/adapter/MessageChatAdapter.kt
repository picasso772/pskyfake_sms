package com.psky.fake_sms.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.psky.fake_sms.R
import com.psky.fake_sms.data.model.MessageChat
import com.psky.fake_sms.utils.Define
import com.psky.fake_sms.utils.append
import kotlinx.android.synthetic.main.header_message_chat.view.*
import kotlinx.android.synthetic.main.message_chat_friend.view.*
import kotlinx.android.synthetic.main.message_chat_me.view.*

class MessageChatAdapter(var items: MutableList<MessageChat>, val context: Context?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM_ME = 1
    private val TYPE_ITEM_FRIEND = 2

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            TYPE_HEADER -> {
                view = LayoutInflater.from(context).inflate(R.layout.header_message_chat, p0, false)
                HeaderViewHolder(view)
            }
            TYPE_ITEM_ME -> {
                view = LayoutInflater.from(context).inflate(R.layout.message_chat_me, p0, false)
                MessageMeViewHolder(view)
            }
            TYPE_ITEM_FRIEND -> {
                view = LayoutInflater.from(context).inflate(R.layout.message_chat_friend, p0, false)
                MessageFriendViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(R.layout.header_message_chat, p0, false)
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
            is MessageFriendViewHolder -> holder.messageFriend.text = items[position].content
            is MessageMeViewHolder -> holder.messageMe.text = items[position].content
        }

    }

    /**
     * Used update the list message
     */
    fun updateMessageChat(item: MessageChat) {
        if (items.size == 0) {
            // Trường hợp 1: Nếu như tin nhắn đầu tiên thì sẽ add một header vào trước khi add item message chat vào
            this.items.append(
                MessageChat(
                    content = "Today ${item.timeString}",
                    timeLong = item.timeLong,
                    timeString = item.timeString,
                    header = 1
                )
            )
        } else {
            // Trường hợp 2: thời gian time trong item lớn hơn phần tử cuối cùng trong items ( >= TIME_MAX ) thì add một header vào trước
            val messageLast = items.get(itemCount - 1)
            if ((item.timeLong - messageLast.timeLong) >= Define.Fields.TIME_MAX) {
                this.items.append(
                    MessageChat(
                        content = "Today ${item.timeString}",
                        timeLong = item.timeLong,
                        timeString = item.timeString,
                        header = 1
                    )
                )
            }
        }
        this.items.append(item)
        notifyItemChanged(itemCount)
    }

    /**
     * Class ViewHolder of Me
     */
    class MessageMeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageMe: TextView = view.messageMe
    }

    /**
     * Class ViewHolder of Friend
     */
    class MessageFriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageFriend: TextView = view.messageFriend
    }

    /**
     * Class ViewHolder of Header
     */
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.header
    }
}