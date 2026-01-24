package com.example.artguess.data.met

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object MetClient {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://collectionapi.metmuseum.org/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: MetApi = retrofit.create(MetApi::class.java)
}
