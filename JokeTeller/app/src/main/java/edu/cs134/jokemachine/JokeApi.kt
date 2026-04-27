package edu.cs134.jokemachine

import retrofit2.http.GET

data class JokeResponse(
    val setup: String,
    val punchline: String
)

interface JokeApi {

    @GET("jokes/programming/random")
    suspend fun getRandomJoke(): List<JokeResponse>

}