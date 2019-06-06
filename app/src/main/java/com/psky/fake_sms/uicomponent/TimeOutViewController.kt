package com.psky.fake_sms.uicomponent

import android.annotation.SuppressLint
import android.os.CountDownTimer
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.chat.ChatViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.service.SingletonService
import com.psky.fake_sms.utils.Utils
import kotlinx.android.synthetic.main.time_out_view_controller.*

/**
 * Create by AnhPQ in 04/06/2019
 */
class TimeOutViewController : BaseFragment() {

    val timeSleep = 1000L

    companion object {
        val shared = SingletonService.get(TimeOutViewController::class)
    }

    lateinit var countDownTimer: CountDownTimer

    var timeOut: Long = 0

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        countDownTimer = object : CountDownTimer((timeOut + 1) * 1000L, timeSleep) {
            override fun onTick(millisUntilFinished: Long) {
                buttonCall.text = "Call (${Utils.shared.formatDuration(millisUntilFinished)})"
            }

            override fun onFinish() {
                ScreenService.shared.removeSubView()
                ScreenService.shared.setSubView(ChatViewController::class)
            }
        }
        countDownTimer.start()
    }

    // region -> Actions

    @OnClick(R.id.buttonCancel)
    fun actionCancel() {
        countDownTimer.cancel()
        ScreenService.shared.removeSubView()
    }

    // endregion
}