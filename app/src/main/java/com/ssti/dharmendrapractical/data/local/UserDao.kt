package com.ssti.dharmendrapractical.data.local

import androidx.room.*
import androidx.room.Dao
import com.ssti.dharmendrapractical.data.model.CartsResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(user: ProfileEntity): Long

    @Query("SELECT * FROM profile WHERE email = :email")
    suspend fun getUserByEmail(email: String): ProfileEntity?

    @Query("SELECT * FROM profile WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): ProfileEntity?

    @Query("UPDATE profile SET name = :name, email = :email, password = :password WHERE id = :id")
    suspend fun updateUserById(id: Int, name: String, email: String, password: String)

    @Query("DELETE FROM profile WHERE id = :id")
    suspend fun deleteUserById(id: Int)

    @Query("SELECT * FROM profile")
    fun getUsers(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profile WHERE id = :id")
    fun getUserById(id: Int): Flow<ProfileEntity?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarts(carts: List<CartsResponse>)

    @Query("SELECT * FROM carts ORDER BY id DESC LIMIT 1")
    suspend fun getCachedCarts(): CartsResponse?

    @Query("SELECT * FROM carts")
    suspend fun getAllCachedCarts(): List<CartsResponse>
}
