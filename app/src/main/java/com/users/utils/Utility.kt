package com.users.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

object Utility {

    /**
     * This is method is used to check network connection.
     * @param context Unused.
     * @return Nothing.
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}