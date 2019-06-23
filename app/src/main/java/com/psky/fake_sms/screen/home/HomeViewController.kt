package com.psky.fake_sms.screen.home

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.psky.fake_sms.R
import com.psky.fake_sms.entity.ScreenHomeType
import com.psky.fake_sms.screen.NavigationScreen
import com.psky.fake_sms.screen.base.BaseFragment
import com.psky.fake_sms.screen.chat.CreateChatViewController
import com.psky.fake_sms.screen.chat.ListChatViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.utils.DataUtils
import com.psky.fake_sms.utils.clearFocusAndHideKeyboard
import com.psky.fake_sms.utils.isHidden
import com.psky.fake_sms.utils.negative
import kotlinx.android.synthetic.main.create_chat_view_controller.*
import kotlinx.android.synthetic.main.home_view_controller.*
import java.io.File

@Suppress("DEPRECATION")
class HomeViewController : BaseFragment(), NavigationScreen {

    private val createChatViewController = CreateChatViewController()
    private val listChatViewController = ListChatViewController()

    var openNavigation : Boolean = true

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        firstLoadScreen()
    }

    override fun onStart() {
        super.onStart()
        buttonNaviPen.isHidden = true
    }

    private var viewType = ScreenHomeType.createChat

    private fun firstLoadScreen() {
        ScreenService.shared.setRootController(createChatViewController)
        createChatViewController.setOnClickListener(this)
    }

    // region -> Actions

    @OnClick(R.id.buttonMenu) fun actionOpenMenu() {
        view?.clearFocusAndHideKeyboard()
        if (openNavigation) {
            animationOpenMenu()
        } else {
            animationCloseMenu()
        }
    }

    @OnClick(R.id.createChat) fun actionCreateChat() {
        if(openNavigation) {
            if (viewType != ScreenHomeType.createChat) {
                viewType = ScreenHomeType.createChat
                uploadViewLayout(ScreenHomeType.createChat)
                ScreenService.shared.setRootController(createChatViewController)
            }
        } else {
            animationCloseMenu()
        }
    }

    @OnClick(R.id.listChat) fun actionListChat() {
        if (viewType != ScreenHomeType.listChat) {
            viewType = ScreenHomeType.listChat
            uploadViewLayout(ScreenHomeType.listChat)
            ScreenService.shared.setRootController(listChatViewController)
        }
    }

    @OnClick(R.id.layoutContent, R.id.header) fun actionLayoutContent() {
        animationCloseMenu()
    }

    @OnClick(R.id.menuHome, R.id.textHome) fun actionHome() {
        titleHeader.text = "Home"
        layoutPrivacyPolicy.visibility = View.GONE
        optionHome.visibility = View.VISIBLE
        containerHome.visibility = View.VISIBLE
        animationCloseMenu()
    }

    @OnClick(R.id.privacyHome, R.id.textPrivacy) fun actionPrivacy() {
        titleHeader.text = "Privacy Policy"
        layoutPrivacyPolicy.visibility = View.VISIBLE
        textPrivacyPolicy.text = Html.fromHtml(resources.getString(R.string.privacy_app))
        optionHome.visibility = View.GONE
        containerHome.visibility = View.GONE
        animationCloseMenu()
    }

    @OnClick(R.id.shareHome, R.id.textShare) fun actionShare() {
        // animationCloseMenu()
        val intent = Intent(android.content.Intent.ACTION_SEND)
        intent.type = "text/plain"
        val body :String = "I found best application to Create Fake Time Results... You must try too... Download Fake Time App Now... \n" +
                "link........"
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body)
        startActivity(intent)
    }

    @OnClick(R.id.supportHome, R.id.textSupport) fun actionSupport() {
        animationCloseMenu()
    }

    // endregion

    private fun clearForm() {
        createChat.setBackgroundResource(R.drawable.bg_option_home_left)
        listChat.setBackgroundResource(R.drawable.bg_option_home_right)

        createChat.setTextColor(ContextCompat.getColor(context!!, R.color.header))
        listChat.setTextColor(ContextCompat.getColor(context!!, R.color.header))
    }

    private fun uploadViewLayout(type: ScreenHomeType) {
        clearForm()
        when (type) {
            ScreenHomeType.createChat -> {
                createChat.setBackgroundResource(R.drawable.bg_option_selected_home_left)
                createChat.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            }
            ScreenHomeType.listChat -> {
                listChat.setBackgroundResource(R.drawable.bg_option_selected_home_right)
                listChat.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            }
        }
    }

    private fun animationOpenMenu() {
        openNavigation = false
        val x = view?.width?.toFloat() ?: 0f
        val params: ViewGroup.LayoutParams = layoutMenu.layoutParams
        params.width = (0.8 * x).toInt()
        layoutMenu.layoutParams = params
        layoutHome.animate().setDuration(300L).translationX(0.8f * x).start()
        createChatViewController.isMenuShowing = true
        DataUtils.shared.backID = 3
    }

    fun animationCloseMenu(){
        openNavigation = true
        createChatViewController.isMenuShowing = false
        layoutHome.animate().setDuration(300L).translationX(0f).start()
    }

    override fun goBack() {
        // TODO: goBack
    }

    // region -> NavigationScreen

    override fun closeLayoutMenu() {
        animationCloseMenu()
    }

    override fun openHomeMenu() {
        animationCloseMenu()
    }

    // endregion


    fun animationHideView() {
        val view = view ?: return
        view.animate().setDuration(300L).translationX(view.width.toFloat().negative).start()
    }

    fun animationShowView() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        createChatViewController.resetImage()
        val view = view ?: return
        view.animate().setDuration(300L).translationX(0f).start()
    }
}