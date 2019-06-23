package com.psky.fake_sms.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.psky.fake_sms.R
import com.psky.fake_sms.entity.ScreenType
import com.psky.fake_sms.screen.chat.ChatViewController
import com.psky.fake_sms.screen.chat.CreateChatViewController
import com.psky.fake_sms.screen.home.HomeViewController
import com.psky.fake_sms.screen.show.detail.LineChatViewController
import com.psky.fake_sms.screen.show.detail.MessagesChatViewController
import com.psky.fake_sms.screen.show.detail.MessengerChatViewController
import com.psky.fake_sms.screen.show.list.*
import com.psky.fake_sms.screen.splash.SplashViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.service.SingletonService
import com.psky.fake_sms.uicomponent.PopupSelectImageView
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.Define
import com.psky.fake_sms.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.View
import java.io.*


class LauncherViewController : AppCompatActivity() {

    companion object {
        val shared = SingletonService.get(LauncherViewController::class)
    }

    val PREFS_FILENAME = "quanganh"
    var prefs: SharedPreferences? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launcher_view_controller)
        ScreenService.setActivity(this)
        addSubScreen()
        // Thời hạn dùng thử
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        when {
            prefs?.getInt("countUse", 0) == 0 -> {
                val editor = prefs!!.edit()
                editor.putInt("countUse", 1)
                editor.apply()
            }
            prefs?.getInt("countUse", 0) == 50 -> finish()
            else -> {
                val count = prefs?.getInt("countUse", 0) ?: 0
                val editor = prefs!!.edit()
                editor.putInt("countUse", count.plus(1))
                editor.apply()
            }
        }
    }

    /**
     * Navigation Screen
     */
    private fun addSubScreen() {
        ScreenService.shared.setRoot(SplashViewController::class)
    }

    /**
     * Reload navigation
     */
    fun reloadScreen(viewId: ScreenType) {
        when (viewId) {
            ScreenType.home -> ScreenService.shared.setRoot(HomeViewController::class)
            ScreenType.result -> ScreenService.shared.setRoot(SplashViewController::class)
        }
    }

    /**
     * Reload Back Screen
     */
    override fun onBackPressed() {
        val backID = DataUtils.shared.backID
        Log.d("TAGs", "backID: $backID")
        when (backID) {
            0 -> super.onBackPressed()
            1 -> {
                DataUtils.shared.backID = 0
                ScreenService.shared.removeSubView()
            }
            2 -> {
                DataUtils.shared.backID = 1
                (supportFragmentManager.fragments.last() as PopupSelectImageView).animationClosePopup()
            }
            3 -> {
                DataUtils.shared.backID = 0
                (supportFragmentManager.fragments.first() as HomeViewController).animationCloseMenu()
            }
            4 -> {
                DataUtils.shared.backID = 0
                DataUtils.shared.resetData()
                // remove view
                ScreenService.shared.removeSubView()
                // animation view
                (supportFragmentManager.fragments.first() as? HomeViewController)?.animationShowView()
                (supportFragmentManager.fragments.last() as? ChatViewController)?.animationHideView()
            }
            5 -> {
                DataUtils.shared.backID = 0
                DataUtils.shared.resetData()
                // animation view
                (supportFragmentManager.fragments.first() as? HomeViewController)?.animationShowView()
                // TODO
                val fragments = supportFragmentManager.fragments
                var frs: ShowListMessagesView? = null
                for (fr in fragments) {
                    if (fr is ShowListMessagesView) {
                        frs = fr
                        break
                    }
                }
                frs?.animationHideView()
            }
            6 -> {
                DataUtils.shared.backID = 0
                DataUtils.shared.resetData()
                // animation view
                (supportFragmentManager.fragments.first() as? HomeViewController)?.animationShowView()
                // TODO
                val fragments = supportFragmentManager.fragments
                var frs: ShowListMessengerView? = null
                for (fr in fragments) {
                    if (fr is ShowListMessengerView) {
                        frs = fr
                        break
                    }
                }
                frs?.animationHideView()
            }
            7 -> {
                DataUtils.shared.backID = 0
                DataUtils.shared.resetData()
                // animation view
                (supportFragmentManager.fragments.first() as? HomeViewController)?.animationShowView()
                // TODO
                val fragments = supportFragmentManager.fragments
                var frs: ShowListLineView? = null
                for (fr in fragments) {
                    if (fr is ShowListLineView) {
                        frs = fr
                        break
                    }
                }
                frs?.animationHideView()
            }
            8 -> {
                DataUtils.shared.backID = 0
                DataUtils.shared.resetData()
                // animation view
                (supportFragmentManager.fragments.first() as? HomeViewController)?.animationShowView()
                // TODO
                val fragments = supportFragmentManager.fragments
                var frs: ShowListWechatView? = null
                for (fr in fragments) {
                    if (fr is ShowListWechatView) {
                        frs = fr
                        break
                    }
                }
                frs?.animationHideView()
            }
            9 -> {
                DataUtils.shared.backID = 0
                DataUtils.shared.resetData()
                // animation view
                (supportFragmentManager.fragments.first() as? HomeViewController)?.animationShowView()
                // TODO
                val fragments = supportFragmentManager.fragments
                var frs: ShowListWhatsappView? = null
                for (fr in fragments) {
                    if (fr is ShowListWhatsappView) {
                        frs = fr
                        break
                    }
                }
                frs?.animationHideView()
            }
            10 -> {
                DataUtils.shared.backID = 5
                DataUtils.shared.resetData()
                // animation view
                // TODO
                val fragments = supportFragmentManager.fragments
                var frs: MessagesChatViewController? = null
                for (fr in fragments) {
                    if (fr is MessagesChatViewController) {
                        frs = fr
                        break
                    }
                }
                frs?.animationHideView()
            }
            11 -> {
                DataUtils.shared.backID = 6
                DataUtils.shared.resetData()
                // animation view
                // TODO
                val fragments = supportFragmentManager.fragments
                var frs: MessengerChatViewController? = null
                for (fr in fragments) {
                    if (fr is MessengerChatViewController) {
                        frs = fr
                        break
                    }
                }
                frs?.animationHideView()
            }
            12 -> {
                DataUtils.shared.backID = 7
                DataUtils.shared.resetData()
                // animation view
                // TODO
                val fragments = supportFragmentManager.fragments
                var frs: LineChatViewController? = null
                for (fr in fragments) {
                    if (fr is LineChatViewController) {
                        frs = fr
                        break
                    }
                }
                frs?.animationHideView()
            }
        }

    }

    @SuppressLint("Recycle", "SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fragments = supportFragmentManager.fragments
        var frCreateChat: CreateChatViewController? = null
        for (fr in fragments) {
            if (fr is CreateChatViewController) {
                frCreateChat = fr
                break
            }
        }
        if (Activity.RESULT_OK == resultCode && requestCode == Define.Fields.REQUEST_CAMERA) {
            val path = DataUtils.shared.pathImage ?: return

            frCreateChat?.showAvatar(path)
        }

        if (Activity.RESULT_OK == resultCode && requestCode == Define.Fields.REQUEST_LIBRARY) {
//            if (data != null && data.data != null) {
//                val uri = data.data ?: return
//                val path = Utils.shared.getPathFromUri(context = this, uri = uri)
//                val file = File(path)
//                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//                val dstPath =
//                    externalCacheDir.toString() + File.separator + "fake_sms" + File.separator + timeStamp + Define.Fields.FORMAT_IMAGE
//                val dstFile = File(dstPath)
//                Utils.shared.handlingImage(file, dstFile)
//
//                DataUtils.shared.pathImage = dstPath
//                frCreateChat?.showAvatar(dstPath)
//            }
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
            if (bitmap == null) {
//                Snackbar.make(this., "Không thể lấy thông tin hình ảnh", Snackbar.LENGTH_SHORT).show()
                Log.d("TAGs", "Khong the lay thong tin hinh anh")
                return
            }
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val dstPath =
                externalCacheDir.toString() + File.separator + "fake_sms" + File.separator + timeStamp + Define.Fields.FORMAT_IMAGE
            val file = File(dstPath)
            if (file.createNewFile()) {
                val os : OutputStream = BufferedOutputStream(FileOutputStream(file))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.close()
            } else {
                Log.d("TAGs", "Loi")
            }

            DataUtils.shared.pathImage = dstPath
            frCreateChat?.showAvatar(dstPath)
        }

        if (Activity.RESULT_CANCELED == resultCode && requestCode == Define.Fields.REQUEST_CAMERA) {
            DataUtils.shared.pathImage = null
        }

        if (Activity.RESULT_OK == resultCode && requestCode == 401) {

            val contact = data?.data ?: return
            val c: Cursor = contentResolver?.query(contact, null, null, null, null) ?: return
            if (c.moveToFirst()) {
                val contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))

                // name
                val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (!TextUtils.isEmpty(name)) {
                    DataUtils.shared.name = name
                }

                // phone
                val hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                var phoneNumber: String? = null
                if ("1" == hasPhone) {
                    val phones = contentResolver?.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null,
                        null
                    ) ?: return
                    while (phones.moveToNext()) {
                        phoneNumber =
                            phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    phones.close()
                    if (phoneNumber != null) {
                        DataUtils.shared.phone = phoneNumber
                    }
                }
                val images = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId.toLong())
                val displayPhotoUri = Uri.withAppendedPath(images, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)
                var bitmap: Bitmap?
                try {
                    val fd = contentResolver?.openAssetFileDescriptor(displayPhotoUri, "r") ?: return
                    bitmap = BitmapFactory.decodeStream(fd.createInputStream())
                } catch (e: IOException) {
                    bitmap = null
                }
                if (bitmap != null) {
                    val file = Utils.shared.createFileImage(this)
                    val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    os.close()
                    DataUtils.shared.pathImage = file.absolutePath
                }

                frCreateChat?.refershData()
            }
        }
    }
}

