package com.example.test.features.recycler_view_with_fragment_type

data class SectionDTO(
    val title: String = "",
    val onClickSeeAll: (() -> Unit)? = null,
)