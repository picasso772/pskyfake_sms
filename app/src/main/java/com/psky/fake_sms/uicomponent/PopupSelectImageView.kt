package com.psky.fake_sms.uicomponent

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import butterknife.OnClick
import com.psky.fake_sms.BuildConfig
import com.psky.fake_sms.R
import com.psky.fake_sms.custom.AppColor
import com.psky.fake_sms.custom.UIColor
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.service.SingletonService
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.Define
import com.psky.fake_sms.utils.Utils
import kotlinx.android.synthetic.main.popup_select_image_view.*
import java.io.File


class PopupSelectImageView : BaseFragment() {

    private val duration: Long = 300L

    companion object{
        val shared = SingletonService.get(PopupSelectImageView::class)
    }

    override fun onStart() {
        super.onStart()
        DataUtils.shared.backID = 2
        popupView.translationY = view?.rootView?.height?.toFloat() ?: 0F
    }

    override fun onResume() {
        super.onResume()
        animationOpenPopup()
    }

    // region -> Actions

    @OnClick(R.id.buttonCancel, R.id.layout) fun actionCancel() {
        animationClosePopup()
    }

    @OnClick(R.id.camera) fun actionOpenCamera() {
        if(!requestPermissionCamera()){
            openCamera()
        }
    }

    @OnClick(R.id.library) fun actionOpenLibrary() {
        if (!requestPermission()) {
            openLibrary()
        }
    }

    @OnClick(R.id.popupView) fun actionPopupView() {
        return
    }

    // endregion

    private fun animationOpenPopup() {
        val colorAnimation = ValueAnimator.ofObject(
            ArgbEvaluator(),
            UIColor.clear.intValue,
            AppColor.dialogBackground.intValue
        )
        colorAnimation.setDuration(duration).addUpdateListener {
            view?.setBackgroundColor(it.animatedValue as? Int ?: UIColor.clear.intValue)
        }
        popupView.animate().setDuration(duration).translationY(0f).withStartAction {
            colorAnimation.setDuration(duration).start()
        }
    }

    fun animationClosePopup() {
        val colorAnimation = ValueAnimator.ofObject(
            ArgbEvaluator(),
            AppColor.dialogBackground.intValue,
            UIColor.clear.intValue
        )
        colorAnimation.setDuration(duration).addUpdateListener {
            view?.setBackgroundColor(it.animatedValue as? Int ?: UIColor.clear.intValue)
        }
        popupView.animate().setDuration(duration).translationY(view?.height?.toFloat() ?: 0F).withStartAction {
            colorAnimation.setDuration(duration).start()
        }.withEndAction {
            ScreenService.shared.removeSubView()
        }
    }

    // region -> Permision

    private fun requestPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        } else {
            return if (isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) && isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                false
            } else {
                requestPermissions(
                    listOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).toTypedArray()
                    , 102
                )
                true
            }
        }
    }

    private fun requestPermissionCamera(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            false
        } else {
            if (isGranted(Manifest.permission.CAMERA)) {
                false
            } else {
                requestPermissions(
                    listOf(
                        Manifest.permission.CAMERA
                    ).toTypedArray()
                    , 103
                )
                true
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openLibrary()
            }
        }
        if (requestCode == 103) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    // endregion

    private fun openLibrary() {
        if (!Utils.shared.existsFakeSMS(context = context!!)) {
            return
        }
        animationClosePopup()

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        if (intent.resolveActivity(context?.packageManager) == null) {
            Snackbar.make(view!!, "Could not open device library", Snackbar.LENGTH_SHORT).show()
            return

        }
        activity?.startActivityForResult(Intent.createChooser(intent, "Select Image"), Define.Fields.REQUEST_LIBRARY)
    }

    private fun openCamera() {
        if (!Utils.shared.existsFakeSMS(context = context!!)) {
            return
        }
        animationClosePopup()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context?.packageManager) != null) {
            val photoFile: File = Utils.shared.createFileImage(context!!)
            DataUtils.shared.pathImage = photoFile.absolutePath
            val photoURI: Uri =
                FileProvider.getUriForFile(activity!!, BuildConfig.APPLICATION_ID + ".provider", photoFile)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            activity?.startActivityForResult(intent, Define.Fields.REQUEST_CAMERA)
        }
    }
}