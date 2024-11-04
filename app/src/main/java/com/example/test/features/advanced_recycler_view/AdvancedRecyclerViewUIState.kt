package com.example.test.features.advanced_recycler_view

import com.example.test.utils.FetchingStatus

data class AdvancedRecyclerViewUIState(
    val fetchingStatus: FetchingStatus = FetchingStatus.INITIAL,
    val postList: ArrayList<PostDTO> = arrayListOf(),
    val msg: String = ""
)
