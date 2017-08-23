package com.jamieadkins.gwent

import android.content.Context

abstract class ConnectionChecker(val context: Context) {
    abstract fun isConnectedToInternet() : Boolean
}