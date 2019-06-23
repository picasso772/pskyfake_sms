package com.psky.fake_sms.screen.chat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.psky.fake_sms.R
import com.psky.fake_sms.data.model.User
import com.psky.fake_sms.entity.NotificationError
import com.psky.fake_sms.screen.NavigationScreen
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.uicomponent.NotificationViewController
import com.psky.fake_sms.uicomponent.PopupSelectImageView
import com.psky.fake_sms.uicomponent.TimeOutViewController
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.clearFocusAndHideKeyboard
import com.psky.fake_sms.utils.hideKeyboard
import kotlinx.android.synthetic.main.create_chat_view_controller.*
import java.io.File

class CreateChatViewController : BaseFragment() {

    var listener: NavigationScreen? = null
    var isMenuShowing = false

    fun showAvatar(path: String) {
        Glide.with(buttonImage)
            .load(File(path))
            .into(buttonImage)
    }

    fun refershData() {
        if (DataUtils.shared.name != null) {
            textFieldName.setText(DataUtils.shared.name)
        }
        if (DataUtils.shared.phone != null) {
            textFieldPhone.setText(DataUtils.shared.phone)
        }
        if (DataUtils.shared.pathImage != null) {
            Glide.with(buttonImage)
                .load(File(DataUtils.shared.pathImage))
                .into(buttonImage)
        }
    }

    override fun onResume() {
        super.onResume()
        if (DataUtils.shared.pathImage != null) {
            showAvatar(DataUtils.shared.pathImage!!)
        }
    }

    fun setOnClickListener(listener: NavigationScreen) {
        this.listener = listener
    }

    // region -> Actions

    @OnClick(R.id.layoutView)
    fun actionLayoutView() {
        if (isMenuShowing) {
            listener?.closeLayoutMenu()
        } else {
            clearFocusEditText()
        }
    }

    @OnClick(R.id.buttonImage)
    fun actionSelectImage() {
        if (isMenuShowing) {
            listener?.closeLayoutMenu()
        } else {
            DataUtils.shared.backID = 1
            clearFocusEditText()

            textFieldName.isSelected = false
            ScreenService.shared.setSubView(PopupSelectImageView::class)
        }
    }

    @OnClick(R.id.imageGetContact)
    fun actionGetContact() {
        // kiem tra quyen da dc cho
        if (!requestPermission()) {
            openContact()
        }
    }

    private fun openContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        activity?.startActivityForResult(intent, 401)
    }

    private fun requestPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        } else {
            return if (isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                && isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && isGranted(Manifest.permission.READ_CONTACTS)
            ) {
                false
            } else {
                requestPermissions(
                    listOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_CONTACTS
                    ).toTypedArray()
                    , 102
                )
                true
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                openContact()
            }
        }
    }

    @OnClick(R.id.buttonStart)
    fun actionStart() {
        val name: String = textFieldName.text.toString().trim()
        val phone: String = textFieldPhone.text.toString().trim()
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

        if (phone.length < 10) {
            DataUtils.shared.notificationErrorID = NotificationError.phoneNotValid
            ScreenService.shared.setSubView(NotificationViewController::class)
            return
        }

        if (numberDelay.isEmpty()) {
            DataUtils.shared.notificationErrorID = NotificationError.timeOutEmpty
            ScreenService.shared.setSubView(NotificationViewController::class)
            return
        }

        if (DataUtils.shared.pathImage == null) {
            DataUtils.shared.notificationErrorID = NotificationError.imageEmpty
            ScreenService.shared.setSubView(NotificationViewController::class)
            return
        }
        DataUtils.shared.user = User(name = name, phone = phone, image = DataUtils.shared.pathImage ?: "")
        ScreenService.shared.setSubView(TimeOutViewController.shared)
        TimeOutViewController.shared.timeOut = numberDelay.toLong()
        clearFocusEditText()
        DataUtils.shared.clearDataTemp()
        DataUtils.shared.pathImage = null
    }

    fun resetImage() {
        if (buttonImage != null) {
            Glide.with(buttonImage)
                .load(R.drawable.home_avatar)
                .into(buttonImage)
        }
    }

    // endregion

    fun clearView() {
        textFieldName.setText("")
        textFieldPhone.setText("")
        textFielNumberDelay.setText("2")
    }

    private fun clearFocusEditText() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            textFieldName.hideKeyboard()
            textFieldPhone.hideKeyboard()
            textFielNumberDelay.hideKeyboard()
        } else {
            textFieldName.clearFocusAndHideKeyboard()
            textFieldPhone.clearFocusAndHideKeyboard()
            textFielNumberDelay.clearFocusAndHideKeyboard()
        }

    }
}