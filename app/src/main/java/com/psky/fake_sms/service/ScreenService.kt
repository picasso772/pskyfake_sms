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

        fun getActivity(): AppCompatActivity? {
            return activity
        }

        val shared = SingletonService.get(ScreenService::class)
    }

    fun <T : Fragment> setRoot(view: KClass<T>) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            replace(R.id.container, view.java.newInstance())
        }
    }

    // region -> setSubView

    fun <T : Fragment> setSubView(view: KClass<T>) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            add(R.id.container, view.java.newInstance(), view.java.simpleName).addToBackStack(null)
        }
    }

    fun <T : Fragment> setSubView(view: T) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            add(R.id.container, view, view.javaClass.simpleName).addToBackStack(null)
        }
    }

    // endregion

    /**
     *
     */
    fun <T : Fragment> setRootController(view: KClass<T>) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            replace(R.id.containerHome, view.java.newInstance())
        }
    }

    /**
     *
     */
    fun <T : Fragment> setRootController(view: T) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            replace(R.id.containerHome, view)
        }
    }

    fun <T : Fragment> setNavigationView(view: KClass<T>) {
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            add(R.id.layoutMenu, view.java.newInstance())
        }
    }

    // region ->

    fun <T : Fragment> setContentView(view:  KClass<T>){
        if (activity == null) return
        activity?.supportFragmentManager?.inTransaction {
            add(R.id.layoutContent, view.java.newInstance())
        }
    }

    // endregion

    fun removeSubView() {
        if (activity == null) return
        activity?.supportFragmentManager?.popBackStack()
    }
}