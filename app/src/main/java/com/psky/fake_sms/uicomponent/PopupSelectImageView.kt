package com.psky.fake_sms.uicomponent

import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.utils.DataUtils


class PopupSelectImageView : BaseFragment() {

    val duration: Long = 3000L

    override fun onStart() {
        super.onStart()

        DataUtils.shared.backID = 1

//        popupView.animate().apply {
//            duration = duration
//            y(view?.width?.toFloat()!! - popupView.width)
//            alpha(1f)
//            start()
//        }
    }
}