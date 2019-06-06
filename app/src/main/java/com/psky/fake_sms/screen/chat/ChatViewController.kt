package com.psky.fake_sms.screen.chat

import android.util.Log
import android.view.View
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.home.createchat.CreateChatViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.DataUtils

/**
 * Create by AnhPQ in 05/06/2019
 */
class ChatViewController : BaseFragment() {
    private lateinit var viewContent: View

    override fun onStart() {
        super.onStart()
        DataUtils.shared.backID = 4
        viewContent = view ?: return
        viewContent.translationX = view?.rootView?.width?.toFloat() ?: 0F
        Log.d("TAGs", " " + activity?.supportFragmentManager?.fragments?.size)
        (activity?.supportFragmentManager?.fragments?.get(1) as CreateChatViewController).clearView()
    }

    override fun onResume() {
        super.onResume()
        animationOpenView()
    }

    private fun animationOpenView() {
        viewContent.animate().setDuration(300L).translationX(0f).start()
    }

    fun animationHideView() {
        viewContent.animate().setDuration(300L).translationX(viewContent.width.toFloat()).withEndAction {
            ScreenService.shared.removeSubView()
        }.start()
    }
}