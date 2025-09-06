package otus.homework.coroutines.services

import otus.homework.coroutines.data.Fact
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {

    @GET("fact")
    suspend fun getCatFact() : Response<Fact>
}