package com.psky.fake_sms.data.datasource

import com.psky.fake_sms.data.model.User
import io.reactivex.Flowable

interface IUserDataSource {
    val allUsers: Flowable<List<User>>
    fun getUserById(id: Int): Flowable<User>
    fun insertUser(vararg user: User)
    fun updatetUser(vararg user: User)
    fun deletetUser(user: User)
    fun deleteAllUser()
}