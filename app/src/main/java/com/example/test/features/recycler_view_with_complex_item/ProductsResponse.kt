package com.example.test.features.recycler_view_with_complex_item

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ProductsResponse(
    @SerializedName("data") val data: List<ProductDTO> = listOf(),
)

@Parcelize
data class ProductDTO(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("name") val name: String? = "",
    @SerializedName("seller_name") val sellerName: String? = "",
    @SerializedName("brand_name") val brandName: String? = "",
    @SerializedName("price") val price: Double? = 0.0,
    @SerializedName("original_price") val originalPrice: Double? = 0.0,
    @SerializedName("badges_new") val badgesNew: List<BadgeModel>? = listOf(),
    @SerializedName("discount_rate") val discountRate: Double? = 0.0,
    @SerializedName("rating_average") val ratingAverage: Double? = 0.0,
    @SerializedName("review_count") val reviewCount: Int? = 0,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = "",
    @SerializedName("video_url") val videoUrl: String? = "",
    @SerializedName("quantity_sold") val quantitySold: QuantitySoldModel? = QuantitySoldModel(),
    @SerializedName("origin") val origin: String? = "",
    @SerializedName("is_top_brand") val isTopBrand: Boolean? = false,
) : Parcelable

@Parcelize
data class BadgeModel(
    @SerializedName("placement") val placement: String? = "",
    @SerializedName("type") val type: String? = "",
    @SerializedName("code") val code: String? = "",
    @SerializedName("icon") val icon: String? = null,
    @SerializedName("icon_width") val iconWidth: Double? = null,
    @SerializedName("icon_height") val iconHeight: Double? = null,
    @SerializedName("text") val text: String? = "",
    @SerializedName("text_color") val textColor: String? = "",
) : Parcelable

@Parcelize
data class QuantitySoldModel(
    @SerializedName("text") val text: String? = "",
    @SerializedName("value") val value: Int? = 0,
) : Parcelable