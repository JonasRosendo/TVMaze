package com.jonasrosendo.shared_logic

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

object NetworkUtils {
    fun getConnectionStatus(context: Context): ConnectionStatus {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getConnectionStatusAndroidMOrNewer(connectivityManager)
        } else {
            getConnectionStatusAndroidLOrOlder(connectivityManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getConnectionStatusAndroidMOrNewer(connectivityManager: ConnectivityManager): ConnectionStatus {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionStatus.WIFI_CONNECTED
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionStatus.CELLULAR_CONNECTED
                else -> ConnectionStatus.DISCONNECTED
            }
        }

        return ConnectionStatus.DISCONNECTED
    }

    private fun getConnectionStatusAndroidLOrOlder(connectivityManager: ConnectivityManager): ConnectionStatus {
        val networks = connectivityManager.allNetworks

        networks.forEach { network ->
            val info = connectivityManager.getNetworkInfo(network)
            return if (info?.isConnected == true) ConnectionStatus.WIFI_CONNECTED else ConnectionStatus.DISCONNECTED
        }

        return ConnectionStatus.DISCONNECTED
    }

    enum class ConnectionStatus {
        WIFI_CONNECTED,
        CELLULAR_CONNECTED,
        DISCONNECTED
    }
}