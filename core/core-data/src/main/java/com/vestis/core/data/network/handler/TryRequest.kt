package com.vestis.core.data.network.handler

import com.vestis.core.common.either.Either
import com.vestis.core.data.exception.DataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes a network request, handling various exceptions and errors.
 *
 * Returns [Either.Right] with the response body if successful, or [Either.Left]
 * with a [DataException] on failure.
 * Handles Cancellation, IO errors, and serialization issues mapping them
 * to the appropriate [DataException] subtype.
 */
suspend inline fun <T> tryRequest(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline request: suspend () -> Response<T>
): Either<DataException, T> = try {
    try {
        withContext(ioDispatcher) { request() }.let {
            if (it.isSuccessful) {
                val body = it.body()

                if (body != null) {
                    Either.Right(body)
                } else {
                    Either.Left(
                        DataException.Network.Unparseable(
                            message = "Unexpected null body"
                        )
                    )
                }
            } else {
                Either.Left(
                    DataException.Network.Unexpected(
                        message = "Failed response"
                    )
                )
            }
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        val exception = when (e) {
            is UnknownHostException -> DataException
                .Network.Unreachable(
                    message = "Socket could not be established",
                    cause = e
                )

            is SocketException -> DataException
                .Network.Unreachable(
                    message = "Socket failed",
                    cause = e
                )

            is SocketTimeoutException -> DataException
                .Network.Unreachable(
                    message = "No internet connection"
                )

            is SSLException -> DataException
                .Network.Unreachable(
                    message = "Socket security error",
                    cause = e
                )

            else -> DataException
                .Network.Unexpected(
                    message = "Unknown IO error",
                    cause = e
                )
        }

        Either.Left(exception)
    } catch (e: SerializationException) {
        Either.Left(
            DataException
                .Network.Unparseable(
                    message = "Could not parse server response",
                    cause = e
                )
        )
    }
} catch (e: Throwable) {
    Either.Left(
        DataException.Unexpected(
            message = e.message
        )
    )
}