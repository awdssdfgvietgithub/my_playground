package com.example.test.remote

import com.example.test.features.advanced_recycler_view.PostResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("posts")
    suspend fun getPosts(
        @Query("_limit") limit: Int,
        @Query("_page") page: Int
    ): Response<ArrayList<PostResponse>>
}