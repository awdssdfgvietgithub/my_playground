package com.example.test.features.recycler_view_with_complex_item

import com.example.test.utils.FetchingStatus

data class RecyclerViewWithComplexItemUIState(
    val fetchingStatus: FetchingStatus = FetchingStatus.INITIAL,
    val data: List<ProductDTO> = arrayListOf(),
    val msg: String = ""
)