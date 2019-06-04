package com.psky.fake_sms.data.repository

import com.psky.fake_sms.data.datasource.IUserDataSource
import com.psky.fake_sms.data.model.User
import io.reactivex.Flowable

/**
 * Created by ANHPQ on 04/06/2019
 */
class UserRepository(private val iUserDataSource: IUserDataSource) : IUserDataSource {

    override val allUsers: Flowable<List<User>>
        get() = iUserDataSource.allUsers

    override fun getUserById(id: Int): Flowable<User> {
        return iUserDataSource.getUserById(id = id)
    }

    override fun insertUser(vararg user: User) {
        iUserDataSource.insertUser(*user)
    }

    override fun updatetUser(vararg user: User) {
        iUserDataSource.updatetUser(*user)
    }

    override fun deletetUser(user: User) {
        iUserDataSource.deletetUser(user = user)
    }

    override fun deleteAllUser() {
        iUserDataSource.deleteAllUser()
    }

    companion object {
        private var mInstance: UserRepository? = null
        fun getInstance(iUserDataSource: IUserDataSource): UserRepository {
            if (mInstance == null) {
                mInstance = UserRepository(iUserDataSource)
            }
            return mInstance!!
        }
    }

}