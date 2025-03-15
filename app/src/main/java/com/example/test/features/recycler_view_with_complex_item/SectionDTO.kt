package com.example.test.features.recycler_view_with_complex_item

import androidx.recyclerview.widget.RecyclerView
import com.example.test.features.recycler_view_with_complex_item.utils.CategoryEnum

data class SectionDTO(
    var category: CategoryEnum,
    var viewMoreAction: () -> Unit,
    val items: ArrayList<ProductsResponse.Product> = arrayListOf(),
    var recyclerViewLayoutManager: RecyclerView.LayoutManager,
    var maxItemsCanView: Int = 7,
    var horAdapter: HorizontalAdapter
)