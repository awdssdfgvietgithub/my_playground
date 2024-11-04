package com.example.test.features.advanced_recycler_view

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("userId") val userId: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("body") val body: String = "",
)

fun PostResponse.mapToUI() = PostDTO(
    id = this.id,
    userId = this.userId,
    title = this.title,
    body = this.body
)
