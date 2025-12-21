package com.ssti.dharmendrapractical.data.repository

import com.ssti.dharmendrapractical.data.local.ProfileEntity
import com.ssti.dharmendrapractical.data.local.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    fun getUsers(): Flow<List<ProfileEntity>> = userDao.getUsers()
    fun getUserById(id: Int): Flow<ProfileEntity?> = userDao.getUserById(id)
    suspend fun registerUser(user: ProfileEntity): Long {
        return userDao.registerUser(user)
    }
    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)

    suspend fun login(email: String, password: String) = userDao.login(email, password)
    suspend fun updateUser(id: Int, name: String, email: String, password: String) = userDao.updateUserById(id, name, email, password)
    suspend fun deleteUser(id: Int) = userDao.deleteUserById(id)
}
