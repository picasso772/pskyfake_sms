package com.psky.fake_sms.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.psky.fake_sms.R
import com.psky.fake_sms.data.model.User
import com.psky.fake_sms.utils.Utils
import com.psky.fake_sms.utils.append
import kotlinx.android.synthetic.main.item_list_wechat.view.*
import java.io.File

class WechatAdapter(val users: MutableList<User>, val context: Context?) :
    RecyclerSwipeAdapter<WechatAdapter.WechatViewHolder>() {

    var listener: ItemClick? = null

    fun setOnClickListener(listener: ItemClick) {
        this.listener = listener
    }

    var holders: MutableList<WechatViewHolder> = mutableListOf()

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): WechatViewHolder {
        return WechatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_wechat, parent, false))
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: WechatViewHolder, position: Int) {
        holder.nameUser.text = users[position].name
        holder.lastMessage.text = users[position].messageLast
        holder.timeMessages.text = Utils.shared.formatDateToStringShowMessenger(users[position].timeMessages ?: 0L)

        Glide.with(holder.imageLogo)
            .load(File(users[position].image))
            .into(holder.imageLogo)

        holder.layoutContent.setOnClickListener {
            if (isCheckSwipeShow()) {
                clearSwipe()
            } else {
                listener?.clickUser(position)
            }
        }

        holder.bottomWraper.setOnClickListener {
            listener?.deleteUser(position)
        }

        holder.ibtnDeleteUser.setOnClickListener {
            listener?.deleteUser(position)
        }

        mItemManger.bindView(holder.itemView, position)

        holders.append(holder)
    }

    class WechatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutContent = view.layoutContent
        val layout = view.layout
        val swipe = view.swipe
        val bottomWraper = view.bottomWraper
        val ibtnDeleteUser = view.ibtnDeleteUser
        val nameUser = view.nameUser
        val lastMessage = view.lastMessage
        val timeMessages = view.timeMessages
        val imageLogo = view.imageLogo
    }

    fun clearSwipe() {
        for (holder in holders) {
            holder.swipe.close()
        }
    }

    private fun isCheckSwipeShow(): Boolean {
        for (holder in holders) {
            if (holder.bottomWraper.visibility == View.VISIBLE)
                return true
        }
        return false
    }

    fun deleteUser(position: Int) {
        users.removeAt(position)
        notifyItemRemoved(position)
        Handler().postDelayed({ closeItem(position) }, 450L)
    }
}