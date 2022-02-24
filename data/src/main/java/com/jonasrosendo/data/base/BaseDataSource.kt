package com.jonasrosendo.data.base

import com.google.gson.Gson
import com.jonasrosendo.shared_logic.exception.ErrorExtras
import com.jonasrosendo.shared_logic.exception.Failure
import com.jonasrosendo.shared_logic.functional.Either
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseDataSource {
    companion object {
        private const val MESSAGE_KEY = "message"
        private const val ERROR_KEY = "error"

        private val gson = Gson()
    }

    suspend fun <T> managedExecution(execution: suspend () -> T): Either<Failure, T> =
        try {
            Either.Right(execution())
        } catch (throwable: Throwable) {
            val failure: Failure = when (throwable) {
                is IOException -> Failure.NetworkConnection
                is HttpException -> {
                    val response = throwable.response()
                    val statusCode = response?.code()
                    val extras = tryParseErrorExtras(statusCode, response)
                    when (statusCode) {
                        in 400..499 -> Failure.FeatureFailure(throwable.message(), extras)
                        else -> Failure.ServerError(extras)
                    }
                }
                is NetworkConnectionInterceptor.NoConnectionException -> Failure.NetworkConnection
                else -> Failure.UnknownError
            }

            Either.Left(failure)
        }

    private suspend fun tryParseErrorExtras(statusCode: Int?, response: Response<*>?) : ErrorExtras? {
        val errorBody = response?.errorBody()
        if (errorBody != null) {
            val apiErrorMessage = suspendCancellableCoroutine<String?> {
                val error = response.errorBody()?.string()
                it.resumeWith(Result.success(error))
            }

            if (apiErrorMessage != null) {
                return try {
                    gson.fromJson(apiErrorMessage, ErrorExtras::class.java)
                } catch (e: Exception) {
                    ErrorExtras(apiErrorMessage, statusCode ?: -1)
                }
            }
            return null
        }
        return null
    }
}