package com.users.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.users.model.Data

@Dao
interface UserDao {

    /**
     * This is method is used to get list of users.
     * @return List of users.
     */
    @Query("Select * from Data")
    fun getAllUsers() : List<Data>

    /**
     * This is method is used to add a user.
     * @return nothing.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(data: List<Data>)
}