package com.jonasrosendo.data.base

import com.jonasrosendo.shared_logic.exception.Failure
import com.jonasrosendo.shared_logic.functional.Either
import java.io.IOException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(call: suspend () -> T): T =
        try {
            call()
        } catch (e: NetworkConnectionInterceptor.NoConnectionException) {
            Either.Left(Failure.NetworkConnection) as T
        } catch (e: IOException) {
            Either.Left(Failure.NetworkConnection) as T
        }
}