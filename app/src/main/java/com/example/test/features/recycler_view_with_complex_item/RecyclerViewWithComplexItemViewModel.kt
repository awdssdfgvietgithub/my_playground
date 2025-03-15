package com.example.test.features.recycler_view_with_complex_item

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.features.recycler_view_with_complex_item.utils.CategoryEnum
import com.example.test.remote.ApiClient
import com.example.test.utils.FetchingStatus.FAILURE
import com.example.test.utils.FetchingStatus.LOADING
import com.example.test.utils.FetchingStatus.SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecyclerViewWithComplexItemViewModel : ViewModel() {
    private val _clothesList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val clothesList = _clothesList.asStateFlow()
    private val _glassesList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val glassesList = _glassesList.asStateFlow()
    private val _flowersList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val flowersList = _flowersList.asStateFlow()
    private val _sneakersList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val sneakersList = _sneakersList.asStateFlow()
    private val _toolsList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val toolsList = _toolsList.asStateFlow()
    private val _tablesList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val tablesList = _tablesList.asStateFlow()
    private val _chairsList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val chairsList = _chairsList.asStateFlow()
    private val _tvList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val tvList = _tvList.asStateFlow()
    private val _bagsList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val bagsList = _bagsList.asStateFlow()
    private val _jeansList = MutableStateFlow(RecyclerViewWithComplexItemUIState())
    val jeansList = _jeansList.asStateFlow()

    fun fetchProductsBy(categoryQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("viet", "Fetching products for categoryQuery: $categoryQuery")
        when (categoryQuery) {
            CategoryEnum.CLOTHES.queryStr -> fetchProducts(CategoryEnum.CLOTHES, _clothesList)
            CategoryEnum.GLASSES.queryStr -> fetchProducts(CategoryEnum.GLASSES, _glassesList)
            CategoryEnum.FLOWERS.queryStr -> fetchProducts(CategoryEnum.FLOWERS, _flowersList)
            CategoryEnum.SNEAKERS.queryStr -> fetchProducts(CategoryEnum.SNEAKERS, _sneakersList)
            CategoryEnum.TOOLS.queryStr -> fetchProducts(CategoryEnum.TOOLS, _toolsList)
            CategoryEnum.TABLE.queryStr -> fetchProducts(CategoryEnum.TABLE, _tablesList)
            CategoryEnum.CHAIR.queryStr -> fetchProducts(CategoryEnum.CHAIR, _chairsList)
            CategoryEnum.TV.queryStr -> fetchProducts(CategoryEnum.TV, _tvList)
            CategoryEnum.BAG.queryStr -> fetchProducts(CategoryEnum.BAG, _bagsList)
            CategoryEnum.JEANS.queryStr -> fetchProducts(CategoryEnum.JEANS, _jeansList)
            else -> {}
        }
    }

    private suspend fun fetchProducts(
        category: CategoryEnum,
        stateFlow: MutableStateFlow<RecyclerViewWithComplexItemUIState>
    ) {
        stateFlow.update { it.copy(fetchingStatus = LOADING) }
        delay(2000)

        val response =
            ApiClient.apiServiceTIKI.fetchProducts("7", "top_seller", "1", category.queryStr)

        stateFlow.update {
            if (response.isSuccessful) {
                it.copy(fetchingStatus = SUCCESS, data = response.body()?.data ?: arrayListOf())
            } else {
                it.copy(fetchingStatus = FAILURE, data = arrayListOf())
            }
        }
    }
}