package com.psky.fake_sms.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.psky.fake_sms.R
import com.psky.fake_sms.data.AppDatabase
import com.psky.fake_sms.data.dao.UserDao
import com.psky.fake_sms.data.model.User
import com.psky.fake_sms.entity.ScreenType
import com.psky.fake_sms.screen.chat.ChatViewController
import com.psky.fake_sms.screen.home.HomeViewController
import com.psky.fake_sms.screen.splash.SplashViewController
import com.psky.fake_sms.service.ScreenService
import com.psky.fake_sms.service.SingletonService
import com.psky.fake_sms.uicomponent.PopupSelectImageView
import com.psky.fake_sms.utils.DataUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LauncherViewController : AppCompatActivity() {

    companion object {
        val shared = SingletonService.get(LauncherViewController::class)
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launcher_view_controller)
        ScreenService.setActivity(this)
        // TODO : FILL_PARENT
        //  window.setFlags(
        //      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        //      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        //  )
        addSubScreen()
    }

    /**
     * Navigation Screen
     */
    private fun addSubScreen() {
        ScreenService.shared.setRoot(SplashViewController::class)
    }

    /**
     * Reload navigation
     */
    fun reloadScreen(viewId: ScreenType) {
        when (viewId) {
            ScreenType.home -> ScreenService.shared.setRoot(HomeViewController::class)
            ScreenType.result -> ScreenService.shared.setRoot(SplashViewController::class)
        }
    }

    /**
     * Reload Back Screen
     */

    override fun onBackPressed() {
        val backID = DataUtils.shared.backID
        Log.d("TAGs", "backID: $backID")
        when (backID) {
            0 -> super.onBackPressed()
            1 -> {
                DataUtils.shared.backID = 0
                ScreenService.shared.removeSubView()
            }
            2 -> {
                DataUtils.shared.backID = 1
                (supportFragmentManager.fragments.last() as PopupSelectImageView).animationClosePopup()
            }
            3 -> {
                DataUtils.shared.backID = 0
                (supportFragmentManager.fragments.first() as HomeViewController).animationCloseMenu()
            }
            4 -> {
                DataUtils.shared.backID = 0
                (supportFragmentManager.fragments.first() as HomeViewController).animationShowView()
                (supportFragmentManager.fragments.last() as ChatViewController).animationHideView()
            }
        }

    }
}

