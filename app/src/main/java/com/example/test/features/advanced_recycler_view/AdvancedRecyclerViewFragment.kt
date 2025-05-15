package com.example.test.features.advanced_recycler_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.FragmentAdvancedRecyclerViewBinding
import com.example.test.utils.FetchingStatus
import kotlinx.coroutines.launch

class AdvancedRecyclerViewFragment : Fragment() {
    private var _binding: FragmentAdvancedRecyclerViewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AdvancedRecyclerViewViewModel by viewModels()
    private lateinit var mAdapter: PostListAdapter
    private val linearLayoutManager = LinearLayoutManager(context)
    private var endlessScrollListener: EndlessRecyclerViewScrollListenerKL? = null
    private var pageIndex = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvancedRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefreshLayout()
        setupScrollTopButton()
        subscribeUI()
    }

    private fun setupScrollTopButton() {
        binding.scrollTopButton.setOnClickListener {
            binding.recyclerViewPostList.smoothScrollToPosition(0)
        }
    }

    override fun onResume() {
        super.onResume()
        resetPagination()
        loadData(pageIndex)
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            resetPagination()
            loadData(pageIndex)
        }
    }

    private fun setupRecyclerView() {
        mAdapter = PostListAdapter{ position ->
            linearLayoutManager.scrollToPositionWithOffset(position, 0)
        }
        endlessScrollListener = object : EndlessRecyclerViewScrollListenerKL(
            linearLayoutManager,
            5
        ) {
            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)

            }

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (totalItemsCount < page * 20) return
                mAdapter.showLoading()
                pageIndex = page + 1
                loadData(pageIndex)
            }
        }

        binding.recyclerViewPostList.apply {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            addOnScrollListener(endlessScrollListener!!)
        }
    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState.fetchingStatus) {
                    FetchingStatus.INITIAL -> { /* No action needed */ }
                    FetchingStatus.LOADING -> handleLoadingState()
                    FetchingStatus.SUCCESS -> handleSuccessState(uiState.postList)
                    FetchingStatus.FAILURE -> handleFailureState()
                }
            }
        }
    }

    private fun handleLoadingState() {
        binding.swipeRefreshLayout.isRefreshing = pageIndex <= 1
        if (pageIndex > 1) mAdapter.showLoading()
    }

    private fun handleSuccessState(postList: List<PostDTO>) {
        mAdapter.hideLoading()
        setShowSwipeRefreshLayout(false)

        if (postList.isEmpty() && pageIndex <= 1) {
            binding.tvEmptyData.visibility = View.VISIBLE
        } else {
            binding.tvEmptyData.visibility = View.GONE
            if (pageIndex <= 1) {
                binding.recyclerViewPostList.scrollToPosition(0)
            }
            mAdapter.setData(postList.toCollection(ArrayList()))
        }
    }

    private fun handleFailureState() {
        setShowSwipeRefreshLayout(false)
        mAdapter.hideLoading()
    }

    private fun setShowSwipeRefreshLayout(isShow: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isShow
    }

    private fun loadData(page: Int = 1) {
        viewModel.fetchPostList(page)
    }

    private fun resetPagination() {
        pageIndex = 1
        endlessScrollListener?.resetState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}