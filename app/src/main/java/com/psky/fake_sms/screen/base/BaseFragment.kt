package com.psky.fake_sms.screen.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.psky.fake_sms.data.AppDatabase
import com.psky.fake_sms.data.dao.MessageDao
import com.psky.fake_sms.data.dao.UserDao
import com.psky.fake_sms.service.SingletonService
import com.psky.fake_sms.utils.toSnakeCase
import kotlin.reflect.KClass

open class BaseFragment : Fragment() {

    protected var db: AppDatabase? = null
    protected var userDao: UserDao? = null
    protected var messageDao : MessageDao? = null

    companion object {
        val shared = SingletonService.get(BaseFragment::class)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutName: String = javaClass.simpleName.toSnakeCase()
        val layoutId = context?.resources?.getIdentifier(layoutName, "layout", context?.packageName) ?: return null
        val view = inflater.inflate(layoutId, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    protected fun isGranted(permison: String): Boolean {
        return ActivityCompat.checkSelfPermission(context!!, permison) === PackageManager.PERMISSION_GRANTED
    }
    open fun goBack() {}
}