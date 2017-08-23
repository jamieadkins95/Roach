package com.jamieadkins.gwent

import android.content.Context
import android.net.ConnectivityManager

class ConnectionCheckerImpl(context: Context) : ConnectionChecker(context) {
    override fun isConnectedToInternet(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}