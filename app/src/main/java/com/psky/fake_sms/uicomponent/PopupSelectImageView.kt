package com.psky.fake_sms.uicomponent

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.custom.AppColor
import com.psky.fake_sms.custom.UIColor
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.service.SingletonService
import com.psky.fake_sms.utils.DataUtils
import kotlinx.android.synthetic.main.popup_select_image_view.*


class PopupSelectImageView : BaseFragment() {

    val duration: Long = 300L

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

    @OnClick(R.id.buttonCancel) fun actionCancel() {
        animationClosePopup()
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
}