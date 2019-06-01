package com.psky.fake_sms.screen.home

import android.view.View
import butterknife.OnClick
import com.psky.fake_sms.R
import com.psky.fake_sms.screen.base.BaseFragment

class NavigationDrawer : BaseFragment() {

    @OnClick(R.id.textHome, R.id.menu_home) fun actionMenu(sender: View){
        // TODO : Home
    }

    @OnClick(R.id.textPrivacy, R.id.privacy_home) fun actionPrivacy(sender: View){
        // TODO : Privacy
    }

    @OnClick(R.id.textShare, R.id.share_home) fun actionShare(sender: View){
        // TODO : Share
    }

    @OnClick(R.id.textSupport, R.id.support_home) fun actionSupport(sender: View){
        // TODO : Support
    }
}