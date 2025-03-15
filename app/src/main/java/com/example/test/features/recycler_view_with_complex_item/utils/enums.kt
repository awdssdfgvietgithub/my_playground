package com.example.test.features.recycler_view_with_complex_item.utils

enum class CategoryEnum(val queryStr: String, val title: String) {
    CLOTHES("clothes", "Clothes"),
    GLASSES("glass", "Glasses"),
    FLOWERS("flowers", "Flowers"),
    SNEAKERS("sneakers", "Sneakers"),
    TOOLS("tools", "Tools"),
    TABLE("tables", "Tables"),
    CHAIR("chairs", "Chairs"),
    TV("tv", "Tv"),
    BAG("bags", "Bags"),
    JEANS("jeans", "Jeans"),
}

enum class BadgeEnum(val type: String) {
    ICON_BADGE("icon_badge"),
    TEXT_ABOVE_PRODUCT_NAME("text_above_product_name"),
    DELIVERY_INFO_BADGE("delivery_info_badge"),
}