package com.example.test.features.recycler_view_with_fragment_type.section_2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.features.advanced_recycler_view.AdvancedRecyclerViewUIState
import com.example.test.features.advanced_recycler_view.PostDTO
import com.example.test.remote.ApiClient
import com.example.test.utils.FetchingStatus.LOADING
import com.example.test.utils.FetchingStatus.SUCCESS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class Section2ViewModel : ViewModel() {
    private var _uiState = MutableStateFlow(AdvancedRecyclerViewUIState())
    val uiState get() = _uiState.asStateFlow()

    fun fetchAllPostList() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update {
            it.copy(fetchingStatus = LOADING)
        }
        delay(3000)

        val response = ApiClient.apiService.getPosts(20, 2)

        if (response.isSuccessful) {
            response.body()?.let { body ->
                if (body.isNotEmpty()) {
                    _uiState.update { list ->
                        list.copy(
                            fetchingStatus = SUCCESS,
                            postList = body.map {
                                PostDTO(
                                    it.id,
                                    it.userId,
                                    it.title,
                                    it.body
                                )
                            }.toCollection(ArrayList())
                        )
                    }
                } else {
                    _uiState.update { list ->
                        list.copy(
                            fetchingStatus = SUCCESS,
                            postList = arrayListOf()
                        )
                    }
                }
            } ?: run {
                _uiState.update { list ->
                    list.copy(
                        fetchingStatus = SUCCESS,
                        postList = arrayListOf()
                    )
                }
            }
        }
    }

    fun getTopTenPostList(data: ArrayList<PostDTO>) = data.take(10)
}