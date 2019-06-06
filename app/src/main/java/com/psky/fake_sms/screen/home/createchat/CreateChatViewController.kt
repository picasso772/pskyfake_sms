package com.psky.fake_sms.screen.home.createchat

import android.util.Log
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.data.AppDatabase
import com.psky.fake_sms.data.model.User
import com.psky.fake_sms.entity.NotificationError
import com.psky.fake_sms.screen.NavigationScreen
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.uicomponent.NotificationViewController
import com.psky.fake_sms.uicomponent.PopupSelectImageView
import com.psky.fake_sms.uicomponent.TimeOutViewController
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.hideKeyboard
import com.psky.fake_sms.utils.toInt
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.create_chat_view_controller.*

class CreateChatViewController : BaseFragment() {

    var listener: NavigationScreen? = null
    var isMenuShowing = false

    fun setOnClickListener(listener: NavigationScreen){
        this.listener = listener
    }

    // region -> Actions

    @OnClick(R.id.layoutView) fun actionLayoutView() {
        if (isMenuShowing){
            listener?.closeLayoutMenu()
        } else {
            textFieldName.hideKeyboard()
            textFieldPhone.hideKeyboard()
            textFielNumberDelay.hideKeyboard()
        }
    }

    @OnClick(R.id.buttonImage) fun actionSelectImage() {
        if (isMenuShowing) {
            listener?.closeLayoutMenu()
        } else {
            DataUtils.shared.backID = 1
            textFieldName.hideKeyboard()
            textFieldPhone.hideKeyboard()

            textFieldName.isSelected = false
            ScreenService.shared.setSubView(PopupSelectImageView::class)
        }
    }

    @OnClick(R.id.buttonStart) fun actionStart() {
        val name: String = textFieldName.text.toString()
        val phone: String = textFieldPhone.text.toString()
        val numberDelay: String = textFielNumberDelay.text.toString()

        if (name.isEmpty()) {
            DataUtils.shared.notificationErrorID = NotificationError.nameEmpty
            ScreenService.shared.setSubView(NotificationViewController::class)
            return
        }

        if (phone.isEmpty()) {
            DataUtils.shared.notificationErrorID = NotificationError.phoneEmpty
            ScreenService.shared.setSubView(NotificationViewController::class)
            return
        }

        if (phone.length < 11) {
            DataUtils.shared.notificationErrorID = NotificationError.phoneNotValid
            ScreenService.shared.setSubView(NotificationViewController::class)
            return
        }

        if (numberDelay.isEmpty()) {
            DataUtils.shared.notificationErrorID = NotificationError.timeOutEmpty
            ScreenService.shared.setSubView(NotificationViewController::class)
            return
        }
        Observable.fromCallable {
            db = AppDatabase.getAppdatabase(context = context!!)
            userDao = db?.userDao()

            val user = User(name = name, phone = phone, image = "qweqweqweq")

            with(userDao) {
                this?.insertUser(user)
            }

            userDao?.getUsers()

        }.doOnNext { list ->
            var finalString = ""
            list?.map {
                finalString += it.name + " - "
            }
            Log.d("TAGs", finalString)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
        ScreenService.shared.setSubView(TimeOutViewController.shared)
        TimeOutViewController.shared.timeOut = numberDelay.toLong()
    }

    // endregion

    fun clearView() {
        textFieldName.setText("")
        textFieldPhone.setText("")
        textFielNumberDelay.setText("2")
    }
}