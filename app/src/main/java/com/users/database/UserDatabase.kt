package com.users.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.users.model.Data

@Database(entities = [Data::class],version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        private var userDatabase: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            if (userDatabase == null) {
                synchronized(this) {
                    userDatabase = Room.databaseBuilder(
                        context, UserDatabase::class.java,
                        "user_database"
                    )
                        .build()
                }
            }
            return userDatabase!!
        }
    }
}