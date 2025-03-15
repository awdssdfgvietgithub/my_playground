package com.example.test.remote

import com.example.test.features.advanced_recycler_view.PostResponse
import com.example.test.features.recycler_view_with_complex_item.ProductsResponse
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

interface ApiServiceTIKI {
    @GET("products")
    suspend fun fetchProducts(
        @Query("limit") limit: String,
        @Query("sort") sort: String,
        @Query("page") page: String,
        @Query("q") q: String,
    ): Response<ProductsResponse>
}