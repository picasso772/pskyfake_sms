package com.psky.fake_sms.uicomponent

import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.utils.DataUtils
import kotlinx.android.synthetic.main.popup_select_image_view.*


class PopupSelectImageView : BaseFragment() {

    val duration: Long = 300L

    override fun onStart() {
        super.onStart()
        DataUtils.shared.backID = 1
        popupView.translationY = view?.rootView?.height?.toFloat() ?: 0F
    }

    override fun onResume() {
        super.onResume()
        popupView.animate().setDuration(duration).translationY(0f).start()
    }
}