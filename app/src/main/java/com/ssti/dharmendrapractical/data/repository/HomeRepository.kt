package com.ssti.dharmendrapractical.data.repository

import android.content.Context
import com.ssti.dharmendrapractical.data.hilt.RemoteService
import com.ssti.dharmendrapractical.data.local.UserDao
import com.ssti.dharmendrapractical.data.model.CartsResponse
import com.ssti.dharmendrapractical.utils.NetworkUtil
import com.ssti.dharmendrapractical.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val userDao: UserDao,
    private val remoteService: RemoteService,
    @ApplicationContext private val context: Context
) {

    fun getCartHomeList(): Flow<Resource<CartsResponse>> = flow {
        emit(Resource.Loading())

        try {
            if (NetworkUtil.isNetworkConnected(context)) {
                // Fetch from API
                val response = remoteService.getCartResponse()

                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    // Save to Room
                    userDao.insertCarts(listOf(data))
                    // Emit API data
                    emit(Resource.Success(data))
                } else {
                    // API failed → load from Room
                    val cachedData = userDao.getCachedCarts()
                    if (cachedData != null) {
                        emit(Resource.Success(cachedData))
                    } else {
                        emit(Resource.Error("Failed to load data"))
                    }
                }
            } else {
                // No Internet → load from Room
                val cachedData = userDao.getCachedCarts()
                if (cachedData != null) {
                    emit(Resource.Success(cachedData))
                } else {
                    emit(Resource.Error("No internet connection & no cached data"))
                }
            }
        } catch (e: Exception) {
            // Exception → load from Room
            val cachedData = userDao.getCachedCarts()
            if (cachedData != null) {
                emit(Resource.Success(cachedData))
            } else {
                emit(Resource.Error(e.localizedMessage ?: "Something went wrong"))
            }
        }
    }.flowOn(Dispatchers.IO)
}
