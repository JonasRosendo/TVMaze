package com.jonasrosendo.data.base

import android.content.Context
import com.jonasrosendo.shared_logic.exception.Failure
import com.jonasrosendo.shared_logic.NetworkUtils
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException


class NetworkConnectionInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        when (NetworkUtils.getConnectionStatus(context)) {
            NetworkUtils.ConnectionStatus.WIFI_CONNECTED,
            NetworkUtils.ConnectionStatus.CELLULAR_CONNECTED -> {
                return chain.proceed(chain.request())
            }
            NetworkUtils.ConnectionStatus.DISCONNECTED -> {
                throw NoConnectionException()
            }
        }
    }

    class NoConnectionException : IOException() {
        override val message: String
            get() = Failure.NETWORK_CONNECTION_ERROR
    }
}
