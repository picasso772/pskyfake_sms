package com.psky.fake_sms.data

import com.psky.fake_sms.data.dao.UserDao
import com.psky.fake_sms.data.datasource.IUserDataSource
import com.psky.fake_sms.data.model.User
import io.reactivex.Flowable

class UserDataSource(private val userDao: UserDao) : IUserDataSource {
    override val allUsers: Flowable<List<User>>
        get() = userDao.getAllUser()

    override fun getUserById(id: Int): Flowable<User> {
        return userDao.getUser(id = id)
    }

    override fun insertUser(vararg user: User) {
        userDao.insertUser(*user)
    }

    override fun updatetUser(vararg user: User) {
        userDao.updateUser(*user)
    }

    override fun deletetUser(user: User) {
        userDao.deleteUser(user = user)
    }

    override fun deleteAllUser() {
        userDao.deleteAllUser()
    }

    companion object {
        private var mInstance: UserDataSource? = null
        fun getInstance(userDao: UserDao): UserDataSource {
            if (mInstance == null) {
                mInstance = UserDataSource(userDao)
            }
            return mInstance!!
        }
    }
}