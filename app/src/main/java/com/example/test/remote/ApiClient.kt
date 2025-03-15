package com.example.test.remote

object ApiClient {
    val apiService: ApiService by lazy {
        RetrofitClient.retrofit.create(ApiService::class.java)
    }

    val apiServiceTIKI: ApiServiceTIKI by lazy {
        RetrofitClient.retrofitTIKI.create(ApiServiceTIKI::class.java)
    }
}