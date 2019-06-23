package com.psky.fake_sms.data.dao

import android.arch.persistence.room.*
import com.psky.fake_sms.data.model.User
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface UserDao {
    /**
     * Perform the insert function
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User) : Long

    /**
     * Perform the update function
     */
    @Update
    fun updateUser(vararg user: User)

    /**
     * Perform the delete function
     */
    @Delete
    fun deleteUser(user: User)

    /**
     * Perform the delete all function
     */
    @Query("DELETE FROM tb_user")
    fun deleteAllUser()

    /**
     * Performs the function of retrieving a user record
     */
    @Query("SELECT count(*) FROM tb_user")
    fun getCountUser(): Long

    /**
     * Performs the function of retrieving a user record
     */
    @Query("SELECT * FROM tb_user")
    fun getUsers(): List<User>

    /**
     * Performs the function of retrieving a user record
     */
    @Query("SELECT  * FROM tb_user WHERE id == :id")
    fun getUser(id: Int): Flowable<User>
}