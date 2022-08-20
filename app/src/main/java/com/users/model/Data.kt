package com.users.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Data(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)
