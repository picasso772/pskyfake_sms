package com.psky.fake_sms.screen.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.adapter.MessageChatAdapter
import com.psky.fake_sms.data.AppDatabase
import com.psky.fake_sms.data.model.MessageChat
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.home.HomeViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.chat_view_controller.*

/**
 * Create by AnhPQ in 05/06/2019
 */
class ChatViewController : BaseFragment() {

    private lateinit var adapter: MessageChatAdapter
    private lateinit var viewContent: View
    private var sender: Int = Define.MessageChat.SENDER_FRIEND

    @SuppressLint("HandlerLeak")
    private val handler = object :Handler(){
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                1 ->  actionGoBack()
            }
        }
    }

    // region -> Life cycle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        DataUtils.shared.backID = 4
        viewContent = view ?: return
        viewContent.translationX = view?.rootView?.width?.toFloat() ?: 0F
        Log.d("TAGs", " " + activity?.supportFragmentManager?.fragments?.size)

        val frs = activity?.supportFragmentManager?.fragments ?: return
        for (fr in frs){
            if(fr is CreateChatViewController){
                fr.clearView()
                break
            }
        }

        recyclerViewMessage.layoutManager = LinearLayoutManager(context!!)

        recyclerViewMessage.setHasFixedSize(true)
        adapter = MessageChatAdapter(mutableListOf(), context = context)

        recyclerViewMessage.adapter = adapter
        editTextMessage.hideKeyboard()

        recyclerViewMessage.viewTreeObserver.addOnGlobalLayoutListener {
            if (recyclerViewMessage != null) {
                if (adapter.itemCount != 0) {
                    recyclerViewMessage.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }
        }

        handler.removeCallbacksAndMessages(null)
        animationOpenView()
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    // endregion

    // region -> Actions

    @OnClick(R.id.buttonSend)
    fun actionsSendMessage(view: View) {
        val message: String? = editTextMessage.text.toString().trim()
        if (message != null && message.isNotEmpty()) {
            val timeLong = System.currentTimeMillis()
            val objectMessage = MessageChat(
                content = message,
                timeString = Utils.shared.formatDateToString(timeLong = timeLong),
                timeLong = timeLong,
                sender = sender
            )
            adapter.updateMessageChat(objectMessage)
            recyclerViewMessage.scrollToPosition(adapter.itemCount - 1)
        }
        clear()
    }

    @OnClick(R.id.buttonSenderFriend)
    fun actionsSenderFriend(view: View) {
        if (sender == Define.MessageChat.SENDER_FRIEND) return
        sender = Define.MessageChat.SENDER_FRIEND
        buttonSenderFriend.setBackgroundResource(R.drawable.bg_textview_chat_left)
        buttonSenderMe.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        buttonSenderFriend.setTextColor(ContextCompat.getColor(context!!, R.color.header))
        buttonSenderMe.setBackgroundColor(0)
    }

    @OnClick(R.id.buttonSenderMe)
    fun actionsSenderMe(view: View) {
        if (sender == Define.MessageChat.SENDER_ME) return
        sender = Define.MessageChat.SENDER_ME
        buttonSenderMe.setBackgroundResource(R.drawable.bg_textview_chat_right)
        buttonSenderMe.setTextColor(ContextCompat.getColor(context!!, R.color.header))
        buttonSenderFriend.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        buttonSenderFriend.setBackgroundColor(0)
    }

    @OnClick(R.id.imageBack)
    fun actionBack(view: View) {
        editTextMessage.clearFocusAndHideKeyboard()
        actionGoBack()
    }

    @OnClick(R.id.buttonSave)
    fun actionSave(view: View) {
        if (DataUtils.shared.user == null) {
            Snackbar.make(view, "Không có thông tin người dùng!", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (adapter.itemCount == 0) {
            Snackbar.make(view, "Chưa có message nào cả!", Snackbar.LENGTH_SHORT).show()
        } else {
            DataUtils.shared.messages = adapter.items

            // save user
            Observable.fromCallable {
                db = AppDatabase.getAppdatabase(context = context!!)
                userDao = db?.userDao()
                messageDao = db?.messageDao()

                val user = DataUtils.shared.user!!
                user.timeMessages = adapter.items.last().timeLong
                user.messageLast = adapter.items.last().content

                with(userDao) {
                    val userId = this?.insertUser(user)?.toInt()
                    Log.d("TAGs", "UserID : $userId")
                    for (item in adapter.items) {
                        item.user_id = userId
                    }
                    with(messageDao) {
                        this?.insertMessages(adapter.items)
                        handler.sendEmptyMessage(1)
                    }
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }

    // endregion

    private fun animationOpenView() {
        viewContent.animate().setDuration(300L).translationX(0f).start()
    }

    fun animationHideView() {
        viewContent.animate().setDuration(300L).translationX(viewContent.width.toFloat()).withEndAction {
            ScreenService.shared.removeSubView()
        }.start()
    }

    private fun clear() {
        editTextMessage.setText("")
    }

    private fun actionGoBack() {
        editTextMessage.clearFocusAndHideKeyboard()
        // reset data
        DataUtils.shared.resetData()
        // remove view
        ScreenService.shared.removeSubView()
        DataUtils.shared.backID = 0
        (fragmentManager?.fragments?.first() as HomeViewController).animationShowView()
        (fragmentManager?.fragments?.last() as ChatViewController).animationHideView()
    }
}