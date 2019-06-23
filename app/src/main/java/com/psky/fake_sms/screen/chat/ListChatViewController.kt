package com.psky.fake_sms.screen.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.data.AppDatabase
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.home.HomeViewController
import com.psky.fake_sms.screen.show.list.*
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.DataUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_chat_view_controller.*

class ListChatViewController : BaseFragment() {

    private lateinit var typeSelect : StatusSelect

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler(){
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                1 -> showChatViewController()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        typeSelect = StatusSelect.MESSAGES
    }

    // region -> Actions

    @OnClick(R.id.layoutMessages) fun actionClickMessages() {
        typeSelect = StatusSelect.MESSAGES
        showSelectedChat()
    }

    @OnClick(R.id.layoutMessenger) fun actionClickMessenger() {
        typeSelect = StatusSelect.MESSENGER
        showSelectedChat()
    }

    @OnClick(R.id.layoutLine) fun actionClickLine() {
        typeSelect = StatusSelect.LINE
        showSelectedChat()
    }

    @OnClick(R.id.layoutWechat) fun actionClickWechat() {
        typeSelect = StatusSelect.WECHAT
        showSelectedChat()
    }

    @OnClick(R.id.layoutWhatsapp) fun actionClickWhatsApp() {
        typeSelect = StatusSelect.WHATSAPP
        showSelectedChat()
    }

    @OnClick(R.id.buttonStart) fun actionStart() {
        // get list user
        handler.removeCallbacksAndMessages(null)
        Observable.fromCallable {
            db = AppDatabase.getAppdatabase(context = context!!)
            userDao = db?.userDao()
            messageDao = db?.messageDao()

            with(userDao) {
                val countUser = this?.getCountUser()
                if (countUser != null){
                    Log.d("TAGs", "Size : $countUser")
                }
                val users = this?.getUsers()
                if (users != null) {
                    DataUtils.shared.users = users
                    Log.d("TAGs", "Size : ${users.size}")
                    handler.sendEmptyMessage(1)
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    // endregion

    /**
     *
     */
    private fun showSelectedChat() {
        stickMessages.visibility = View.GONE
        stickMessenger.visibility = View.GONE
        stickLine.visibility = View.GONE
        stickWechat.visibility = View.GONE
        stickWhatsApp.visibility = View.GONE

        when (typeSelect) {
            StatusSelect.MESSAGES -> {
                stickMessages.visibility = View.VISIBLE
            }
            StatusSelect.MESSENGER -> {
                stickMessenger.visibility = View.VISIBLE
            }
            StatusSelect.LINE -> {
                stickLine.visibility = View.VISIBLE
            }
            StatusSelect.WECHAT -> {
                stickWechat.visibility = View.VISIBLE
            }
            StatusSelect.WHATSAPP -> {
                stickWhatsApp.visibility = View.VISIBLE
            }
        }
    }

    /**
     *
     */
    fun showChatViewController() {

        if (DataUtils.shared.users != null) {

            when (typeSelect) {
                StatusSelect.MESSAGES -> {
                    ScreenService.shared.setSubView(ShowListMessagesView::class)
                    (activity?.supportFragmentManager?.fragments?.first() as HomeViewController).animationHideView()
                }
                StatusSelect.MESSENGER -> {
                    ScreenService.shared.setSubView(ShowListMessengerView::class)
                    (activity?.supportFragmentManager?.fragments?.first() as HomeViewController).animationHideView()
                }
                StatusSelect.LINE -> {
                    ScreenService.shared.setSubView(ShowListLineView::class)
                    (activity?.supportFragmentManager?.fragments?.first() as HomeViewController).animationHideView()
                }
                StatusSelect.WECHAT -> {
                    ScreenService.shared.setSubView(ShowListWechatView::class)
                    (activity?.supportFragmentManager?.fragments?.first() as HomeViewController).animationHideView()
                }
                StatusSelect.WHATSAPP -> {
                    ScreenService.shared.setSubView(ShowListWhatsappView::class)
                    (activity?.supportFragmentManager?.fragments?.first() as HomeViewController).animationHideView()
                }
            }
        }
    }
}

enum class StatusSelect {
    MESSAGES,
    MESSENGER,
    LINE,
    WECHAT,
    WHATSAPP;
}