package com.example.test.features.recycler_view_with_fragment_type.section_9

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.databinding.ItemFragment3Binding
import com.example.test.features.recycler_view_with_fragment_type.PostListAdapter
import com.example.test.features.recycler_view_with_fragment_type.section_1.Section1ViewModel
import com.example.test.utils.FetchingStatus
import kotlinx.coroutines.launch

class Section9Fragment : Fragment() {
    private var _binding: ItemFragment3Binding? = null
    private val binding get() = _binding!!
    private val viewModel: Section1ViewModel by viewModels()

    private lateinit var mLayoutManager: LinearLayoutManager
    private val mAdapter by lazy {
        PostListAdapter(PostListAdapter.VIEW_ITEM_POST_V3_TYPE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemFragment3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchAllPostList()

        mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        subscribeUI()
        setupView()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
    }

    private fun setupView() {
        with(binding) {
            textViewTitle.text = "Section 1"
            textViewSeeAll.setOnClickListener {

            }
        }
    }

    private fun subscribeUI() {
        with(binding) {
            lifecycleScope.launch {
                viewModel.uiState.collect { uiState ->
                    when (uiState.fetchingStatus) {
                        FetchingStatus.INITIAL -> { /* No action needed */
                        }

                        FetchingStatus.LOADING -> {
                            progressCircular.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                        }

                        FetchingStatus.SUCCESS -> {
                            mAdapter.setData(uiState.postList)
                            progressCircular.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                        }

                        FetchingStatus.FAILURE -> { /* No action needed */
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}