package com.psky.fake_sms.screen.show.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.adapter.ItemClick
import com.psky.fake_sms.adapter.WhatsappAdapter
import com.psky.fake_sms.data.AppDatabase
import com.psky.fake_sms.data.model.User
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.home.HomeViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.DataUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.show_list_whatsapp_view.*

class ShowListWhatsappView : BaseFragment(), ItemClick{

    private lateinit var adapter: WhatsappAdapter
    private lateinit var viewContent: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataUtils.shared.backID = 9

        viewContent = view
        viewContent.translationX = view.rootView?.width?.toFloat() ?: 0F

        recyclerMessages.layoutManager = LinearLayoutManager(context!!)
        recyclerMessages.setHasFixedSize(true)

        adapter = WhatsappAdapter(DataUtils.shared.users?.toMutableList() ?: mutableListOf(), context = context)
        adapter.setOnClickListener(this)
        recyclerMessages.adapter = adapter

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
        (fragmentManager?.fragments?.last() as ShowListWhatsappView).animationHideView()
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
        Log.d("TAGs", "click User $position")
    }
}