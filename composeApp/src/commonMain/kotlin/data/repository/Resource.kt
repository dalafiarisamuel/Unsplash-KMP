package data.repository

sealed class Resource<out R> {
    data class Success<out R>(val result: R) : Resource<R>()
    data class Failure(val error: Throwable) : Resource<Nothing>()
}

suspend fun <R> resourceHelper(body: suspend () -> R): Resource<R> {
    return try {
        Resource.Success(body())
    } catch (e: Exception) {
        Resource.Failure(e)
    }
}
