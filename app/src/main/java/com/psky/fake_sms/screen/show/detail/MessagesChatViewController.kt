package com.psky.fake_sms.screen.show.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.psky.fake_sms.R
import com.psky.fake_sms.adapter.MessageChatAdapter
import com.psky.fake_sms.adapter.show.MessagesShowChatAdapter
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.chat.CreateChatViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.hideKeyboard
import kotlinx.android.synthetic.main.messages_chat_view_controller.*
import java.io.File

class MessagesChatViewController : BaseFragment() {

    private lateinit var adapter: MessagesShowChatAdapter
    private lateinit var viewContent: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataUtils.shared.backID = 10
        viewContent = view
        viewContent.translationX = view.rootView?.width?.toFloat() ?: 0F

        animationOpenView()
        initView()
    }

    @OnClick(R.id.imageBack) fun actionBack() {
        // reset data
        DataUtils.shared.resetData()
        // remove view
        DataUtils.shared.backID = 5
        // TODO
        val fragments = fragmentManager?.fragments ?: return
        var frMessagesChat: MessagesChatViewController? = null
        for (fr in fragments) {
            if (fr is MessagesChatViewController) {
                frMessagesChat = fr
                break
            }
        }

        frMessagesChat?.animationHideView()
    }

    private fun initView() {
        val user = DataUtils.shared.user ?: return
        Glide.with(imageLogoUser)
            .load(File(user.image))
            .into(imageLogoUser)

        textNameUser.text = user.name + " >"

        recyclerViewMessage.layoutManager = LinearLayoutManager(context!!)

        recyclerViewMessage.setHasFixedSize(true)
        val messages = DataUtils.shared.messages ?: return
        adapter = MessagesShowChatAdapter(messages, context = context)

        recyclerViewMessage.adapter = adapter
        editTextMessage.hideKeyboard()
    }

    private fun animationOpenView() {
        DataUtils.shared.backID = 10
        viewContent.animate().setDuration(300L).translationX(0f).start()
    }

    fun animationHideView() {
        viewContent.animate().setDuration(300L).translationX(viewContent.width.toFloat()).withEndAction {
            ScreenService.shared.removeSubView()
        }.start()
    }
}