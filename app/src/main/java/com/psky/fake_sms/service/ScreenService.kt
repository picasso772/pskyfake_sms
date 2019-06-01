package com.psky.fake_sms.service

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.psky.fake_sms.R
import com.psky.fake_sms.utils.inTransaction
import kotlin.reflect.KClass

@SuppressLint("StaticFieldLeak")
class ScreenService {
    companion object {
        private var activity: AppCompatActivity? = null

        fun setActivity(activity: AppCompatActivity?) {
            ScreenService.activity = activity
        }

        val shared = SingletonService.get(ScreenService::class)
    }

    fun <T : Fragment> setRoot(view: KClass<T>) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            replace(R.id.container, view.java.newInstance())
        }
    }

    fun <T : Fragment> setSubView(view: KClass<T>) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            add(R.id.container, view.java.newInstance()).addToBackStack(null)
        }
    }

    fun <T : Fragment> setRootController(view: KClass<T>) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            replace(R.id.containerHome, view.java.newInstance())
        }
    }

    fun <T : Fragment> setNavigationView(view: KClass<T>) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            add(R.id.layoutMenu, view.java.newInstance())
        }
    }

    fun removeSubView() {
        if (activity == null) return
        activity?.supportFragmentManager?.popBackStack()
    }
}