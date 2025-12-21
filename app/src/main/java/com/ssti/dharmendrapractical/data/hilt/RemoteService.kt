package com.ssti.dharmendrapractical.data.hilt

import com.ssti.dharmendrapractical.data.model.CartsResponse
import retrofit2.Response
import retrofit2.http.GET
interface RemoteService {
    @GET("carts")
    suspend fun getCartResponse(): Response<CartsResponse>
}