package utils

import retrofit2.Call
import java.io.IOException

private fun <T> executeWithRetry(
    callFactory: () -> Call<T>,
    maxAttempts: Int = 3,
    delayMs: Long = 1000
): T {
    var lastException: Exception? = null

    repeat(maxAttempts) { attempt ->
        try {
            val call = callFactory()
            val response = call.execute()

            if (response.isSuccessful) {
                return response.body() ?: throw IllegalStateException("Empty response body")
            }

            if (response.code() == 404) {
                Thread.sleep(delayMs)
                return@repeat // Продолжаем попытки для 404
            }

            throw IOException("HTTP ${response.code()}: ${response.errorBody()?.string()}")

        } catch (e: Exception) {
            lastException = e
            if (attempt < maxAttempts - 1) {
                Thread.sleep(delayMs * (attempt + 1)) // Увеличиваем задержку
            }
        }
    }

    throw lastException ?: IllegalStateException("All attempts failed")
}