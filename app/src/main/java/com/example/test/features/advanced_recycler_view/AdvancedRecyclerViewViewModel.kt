package com.example.test.features.advanced_recycler_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.remote.ApiClient
import com.example.test.utils.FetchingStatus.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdvancedRecyclerViewViewModel : ViewModel() {
    private var _uiState = MutableStateFlow(AdvancedRecyclerViewUIState())
    val uiState get() = _uiState.asStateFlow()

    fun fetchPostList(pageIndex: Int = 1) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update {
            it.copy(fetchingStatus = LOADING)
        }
        delay(2000)

        val response = ApiClient.apiService.getPosts(20, pageIndex)

        if (response.isSuccessful) {
            if (pageIndex > 1) {
                val currentList = uiState.value.postList
                response.body()?.let { body ->
                    if (body.isNotEmpty()) {
                        currentList.addAll(body.map {
                            PostDTO(
                                it.id,
                                it.userId,
                                it.title,
                                it.body
                            )
                        }.toCollection(ArrayList()))
                        _uiState.update { list ->
                            list.copy(
                                fetchingStatus = SUCCESS,
                                postList = currentList
                            )
                        }
                    } else {
                        _uiState.update { list ->
                            list.copy(
                                fetchingStatus = SUCCESS,
                                postList = currentList
                            )
                        }
                    }
                } ?: run {
                    _uiState.update { list ->
                        list.copy(
                            fetchingStatus = SUCCESS,
                            postList = currentList
                        )
                    }
                }
            } else {
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
    }
}