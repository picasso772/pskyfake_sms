package com.psky.fake_sms.screen.show.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.adapter.ItemClick
import com.psky.fake_sms.adapter.MessagesAdapter
import com.psky.fake_sms.data.AppDatabase
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.home.HomeViewController
import com.psky.fake_sms.screen.show.detail.MessagesChatViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.DataUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.show_list_messages_view.*

class ShowListMessagesView : BaseFragment(), ItemClick {

    private lateinit var adapter: MessagesAdapter
    private lateinit var viewContent: View

    @SuppressLint("HandlerLeak")
    private val handler = object :Handler(){
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                1 ->  {
                    ScreenService.shared.setSubView(MessagesChatViewController::class)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataUtils.shared.backID = 5

        recyclerMessages.layoutManager = LinearLayoutManager(context!!)
        recyclerMessages.setHasFixedSize(true)

        adapter = MessagesAdapter(DataUtils.shared.users?.toMutableList() ?: mutableListOf(), context = context)
        adapter.setOnClickListener(this)
        recyclerMessages.adapter = adapter

        viewContent = view
        viewContent.translationX = view.rootView?.width?.toFloat() ?: 0F

        animationOpenView()
    }

    private fun animationOpenView() {
        viewContent.animate().setDuration(300L).translationX(0f).start()
    }

    @OnClick(R.id.buttonBack) fun actionBack() {
        // reset data
        DataUtils.shared.resetData()
        // remove view
        DataUtils.shared.backID = 0
        (fragmentManager?.fragments?.first() as HomeViewController).animationShowView()
        // TODO
        val fragments = fragmentManager?.fragments ?: return
        var frs: ShowListMessagesView? = null
        for (fr in fragments) {
            if (fr is ShowListMessagesView) {
                frs = fr
                break
            }
        }
        frs?.animationHideView()
    }

    fun animationHideView() {
        viewContent.animate().setDuration(300L).translationX(viewContent.width.toFloat()).withEndAction {
            ScreenService.shared.removeSubView()
        }
    }

    override fun deleteUser(position: Int) {
        Observable.fromCallable {
            db = AppDatabase.getAppdatabase(context = context!!)
            userDao = db?.userDao()
            messageDao = db?.messageDao()

            val user = DataUtils.shared.users?.get(position) ?: return@fromCallable

            with(messageDao) {
                val userId = user.id ?: return@fromCallable
                this?.deleteMessagesByUser(userId = userId)
            }

            with(userDao) {
                this?.deleteUser(user)
                DataUtils.shared.users = userDao?.getUsers()
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        adapter.deleteUser(position)
        adapter.holders.removeAt(position)
        adapter.clearSwipe()
    }

    override fun clickUser(position: Int) {
        // Load dữ liệu trước đã ... sau đó Handler thông báo lên MainThread
        val user = DataUtils.shared.users?.get(position)
        Observable.fromCallable {
            db = AppDatabase.getAppdatabase(context = context!!)
            messageDao = db?.messageDao()

            with(messageDao) {
                val userId = user?.id ?: return@fromCallable
                DataUtils.shared.messages = this?.getMessagesByUser(userId = userId)?.toMutableList()
                if (DataUtils.shared.messages != null) {
                    DataUtils.shared.user = user
                    handler.sendEmptyMessage(1)
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}