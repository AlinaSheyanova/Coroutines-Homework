package otus.homework.coroutines.data

sealed class Result{
    data class Success(val data: Info) : Result()
    data class Error(val message: String) : Result()
}
