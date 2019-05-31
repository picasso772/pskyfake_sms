package com.psky.fake_sms.utils

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

/**
 *
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction){
    beginTransaction().func().commit()
}