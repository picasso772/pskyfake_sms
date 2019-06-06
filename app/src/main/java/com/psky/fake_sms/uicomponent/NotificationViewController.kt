package com.psky.fake_sms.uicomponent

import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.entity.NotificationError
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.localizedString
import kotlinx.android.synthetic.main.notification_view_controller.*

/**
 * Create by AnhPQ in 04/06/2019
 */
class NotificationViewController : BaseFragment() {

    // region -> Life cycle

    override fun onStart() {
        super.onStart()
        when (DataUtils.shared.notificationErrorID) {
            NotificationError.nameEmpty -> {
                tvMessage.text = "NAME_EMPTY".localizedString(context = context)
            }
            NotificationError.phoneEmpty -> {
                tvMessage.text = "PHONE_EMPTY".localizedString(context = context)
            }
            NotificationError.phoneNotValid -> {
                tvMessage.text = "PHONE_NOT_VALID".localizedString(context = context)
            }
            NotificationError.timeOutEmpty -> {
                tvMessage.text = "TIME_OUT_EMPTY".localizedString(context = context)
            }
            NotificationError.imageEmpty -> {
                tvMessage.text = "IMAGE_EMPTY".localizedString(context = context)
            }
            else -> {
                tvMessage.text = ""
            }
        }
    }

    // endregion

    // region -> Actions

    @OnClick(R.id.buttonOK) fun actionOK() {
        ScreenService.shared.removeSubView()
    }

    // endregion
}