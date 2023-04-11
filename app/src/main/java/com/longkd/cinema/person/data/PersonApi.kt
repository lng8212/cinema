package com.longkd.cinema.person.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonApi {

    @GET("person/{person_Id}")
    suspend fun getMovieDetails(
        @Path("person_Id") movieId: Int,
        @Query("language") language: String
    ): Response<PersonResponse>

}