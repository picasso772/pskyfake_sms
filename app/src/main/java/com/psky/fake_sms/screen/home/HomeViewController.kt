package com.psky.fake_sms.screen.home

import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.entity.ScreenHomeType
import com.psky.fake_sms.screen.NavigationScreen
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.home.createchat.CreateChatViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.isHidden
import kotlinx.android.synthetic.main.home_view_controller.*

class HomeViewController : BaseFragment(), NavigationScreen {

    val createChatViewController = CreateChatViewController()
    var openNavigation : Boolean = true

    override fun onStart() {
        super.onStart()
        buttonNaviPen.isHidden = true
        firstLoadScreen()
    }

    private var viewType = ScreenHomeType.createChat

    private fun firstLoadScreen() {
        ScreenService.shared.setRootController(createChatViewController)
        createChatViewController.setOnClickListener(this)
    }

    // region -> Actions

    @OnClick(R.id.buttonMenu) fun actionOpenMenu() {
        if (openNavigation) {
            openNavigation = false
            val x = view?.width?.toFloat() ?: 0f
            val params: ViewGroup.LayoutParams = layoutMenu.layoutParams
            params.width = (0.8 * x).toInt()
            layoutMenu.layoutParams = params
            layoutHome.animate().setDuration(300L).translationX(0.8f * x).start()
            createChatViewController.isMenuShowing = true
        } else {
            animationCloseNavigation()
        }
    }

    @OnClick(R.id.createChat) fun actionCreateChat() {
        if(openNavigation) {
            if (viewType != ScreenHomeType.createChat) {
                viewType = ScreenHomeType.createChat
                uploadViewLayout(ScreenHomeType.createChat)
            }
        } else {
            animationCloseNavigation()
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

    @OnClick(R.id.layoutContent, R.id.header) fun actionLayoutContent() {
        animationCloseNavigation()
    }

    @OnClick(R.id.menuHome, R.id.textHome) fun actionHome() {
        animationCloseNavigation()
    }

    @OnClick(R.id.privacyHome, R.id.textPrivacy) fun actionPrivacy() {
        animationCloseNavigation()
    }

    @OnClick(R.id.shareHome, R.id.textShare) fun actionShare() {
        animationCloseNavigation()
    }

    @OnClick(R.id.supportHome, R.id.textSupport) fun actionSupport() {
        animationCloseNavigation()
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

    private fun animationCloseNavigation(){
        openNavigation = true
        createChatViewController.isMenuShowing = false
        layoutHome.animate().setDuration(300L).translationX(0f).start()
    }

    override fun goBack() {
        // TODO: goBack
    }

    // region -> NavigationScreen

    override fun closeLayoutMenu() {
        animationCloseNavigation()
    }

    override fun openHomeMenu() {
        animationCloseNavigation()
    }

    // endregion
}