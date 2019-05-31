package com.psky.fake_sms.screen.splash

import android.os.Handler
import com.psky.fake_sms.entity.ScreenType
import com.psky.fake_sms.screen.LauncherViewController
import com.psky.fake_sms.screen.base.BaseFragment

class SplashViewController : BaseFragment() {

    // region -> Variables

    val handler = Handler()
    val postDelayDuration: Long = 2000

    // endregion

    // region -> Life Cycle

    override fun onStart() {
        super.onStart()
        handler.postDelayed({
            LauncherViewController.shared.reloadScreen(ScreenType.valueOf(value = "home"))
        }, postDelayDuration)
    }

    // endregion
}
