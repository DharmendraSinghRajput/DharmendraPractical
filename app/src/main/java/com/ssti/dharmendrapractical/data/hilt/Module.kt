package com.ssti.dharmendrapractical.data.hilt

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.ssti.dharmendrapractical.R
import com.ssti.dharmendrapractical.data.local.AppDatabase
import com.ssti.dharmendrapractical.data.local.UserDao
import com.ssti.dharmendrapractical.utils.PrefUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideService(): RemoteService {
        val okHttpClientBuilder = OkHttpClient.Builder()

        okHttpClientBuilder.connectTimeout(50, TimeUnit.MINUTES)
        okHttpClientBuilder.writeTimeout(50, TimeUnit.MINUTES)
        okHttpClientBuilder.readTimeout(50, TimeUnit.MINUTES)

        okHttpClientBuilder.addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        okHttpClientBuilder.addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        })

        val retrofit = Retrofit.Builder().client(okHttpClientBuilder.build())
            .baseUrl(APIConst.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(RemoteService::class.java)
    }

   @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences("${context.getString(R.string.app_name)}_PREF", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providePrefUtil(sharedPreferences: SharedPreferences) = PrefUtil(sharedPreferences)

}