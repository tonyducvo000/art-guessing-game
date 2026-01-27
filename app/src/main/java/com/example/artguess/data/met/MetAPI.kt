package com.example.artguess.data.met

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetApi {

    @GET("public/collection/v1/search")
    suspend fun search(
        @Query("q") q: String = "European Paintings",
        @Query("hasImages") hasImages: Boolean = true,
        @Query("departmentId") departmentId: Int = 11
    ): MetSearchResponse

    @GET("public/collection/v1/objects/{objectId}")
    suspend fun objectById(@Path("objectId") objectId: Int): MetObjectResponse
}
