package com.psky.fake_sms.screen.home

import android.support.v4.content.ContextCompat
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.entity.ScreenHomeType
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.home.chat.CreateChatViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.isHidden
import kotlinx.android.synthetic.main.home_view_controller.*

class HomeViewController : BaseFragment() {

    override fun onStart() {
        super.onStart()
        buttonNaviPen.isHidden = true
        firstLoadScreen()
    }

    private var viewType = ScreenHomeType.createChat

    private fun firstLoadScreen() {
        ScreenService.shared.setRootController(CreateChatViewController::class)
    }

    // region -> Actions

    @OnClick(R.id.createChat) fun actionCreateChat() {
        if (viewType != ScreenHomeType.createChat) {
            viewType = ScreenHomeType.createChat
            uploadViewLayout(ScreenHomeType.createChat)
        }
    }

    @OnClick(R.id.listChat) fun actionListChat() {
        if (viewType != ScreenHomeType.listChat) {
            viewType = ScreenHomeType.listChat
            uploadViewLayout(ScreenHomeType.listChat)
        }
    }

    @OnClick(R.id.listCall) fun actionListCall() {
        if (viewType != ScreenHomeType.listCall) {
            viewType = ScreenHomeType.listCall
            uploadViewLayout(ScreenHomeType.listCall)
        }
    }

    // endregion

    private fun clearForm() {
        createChat.setBackgroundResource(R.drawable.bg_option_home_left)
        listChat.setBackgroundResource(R.drawable.bg_option_home_center)
        listCall.setBackgroundResource(R.drawable.bg_option_home_right)

        createChat.setTextColor(ContextCompat.getColor(context!!, R.color.header))
        listChat.setTextColor(ContextCompat.getColor(context!!, R.color.header))
        listCall.setTextColor(ContextCompat.getColor(context!!, R.color.header))
    }

    private fun uploadViewLayout(type: ScreenHomeType) {
        clearForm()
        when (type) {
            ScreenHomeType.createChat -> {
                createChat.setBackgroundResource(R.drawable.bg_option_selected_home_left)
                createChat.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            }
            ScreenHomeType.listChat -> {
                listChat.setBackgroundResource(R.drawable.bg_option_selected_home_center)
                listChat.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            }
            ScreenHomeType.listCall -> {
                listCall.setBackgroundResource(R.drawable.bg_option_selected_home_right)
                listCall.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            }
        }
    }

    override fun goBack() {
        // TODO: goBack
    }
}