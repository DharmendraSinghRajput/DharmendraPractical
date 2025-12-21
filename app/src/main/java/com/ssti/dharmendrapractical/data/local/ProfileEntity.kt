package com.ssti.dharmendrapractical.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "profile",
    indices = [Index(value = ["email"], unique = true)]
)
data class ProfileEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "imageUri")
    val imageUri: String? = null

)
