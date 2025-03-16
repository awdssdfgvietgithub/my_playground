package com.example.test.features.recycler_view_with_complex_item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.databinding.FragmentRecyclerViewWithComplexItemBinding
import com.example.test.features.recycler_view_with_complex_item.utils.CategoryEnum
import com.example.test.features.recycler_view_with_complex_item.utils.setMarginTop
import com.example.test.utils.FetchingStatus
import kotlinx.coroutines.launch

class RecyclerViewWithComplexItemFragment : Fragment() {
    private var _binding: FragmentRecyclerViewWithComplexItemBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecyclerViewWithComplexItemViewModel by viewModels()

    private val mAdapter by lazy {
        SectionsAdapter(object : SectionsAdapter.OnSectionVisibleListener {
            override fun onSectionVisible(queryStr: String) {
                viewModel.fetchProductsBy(queryStr)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewWithComplexItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.toolBar.setMarginTop(systemBars.top)
            WindowInsetsCompat.CONSUMED
        }
        initViews()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            viewModel.clothesList.collect { uiState ->
                handleState(uiState, CategoryEnum.CLOTHES.title)
            }
        }

        lifecycleScope.launch {
            viewModel.glassesList.collect { uiState ->
                handleState(uiState, CategoryEnum.GLASSES.title)
            }
        }

        lifecycleScope.launch {
            viewModel.flowersList.collect { uiState ->
                handleState(uiState, CategoryEnum.FLOWERS.title)
            }
        }

        lifecycleScope.launch {
            viewModel.sneakersList.collect { uiState ->
                handleState(uiState, CategoryEnum.SNEAKERS.title)
            }
        }

        lifecycleScope.launch {
            viewModel.toolsList.collect { uiState ->
                handleState(uiState, CategoryEnum.TOOLS.title)
            }
        }

        lifecycleScope.launch {
            viewModel.tablesList.collect { uiState ->
                handleState(uiState, CategoryEnum.TABLE.title)
            }
        }

        lifecycleScope.launch {
            viewModel.chairsList.collect { uiState ->
                handleState(uiState, CategoryEnum.CHAIR.title)
            }
        }

        lifecycleScope.launch {
            viewModel.tvList.collect { uiState ->
                handleState(uiState, CategoryEnum.TV.title)
            }
        }

        lifecycleScope.launch {
            viewModel.bagsList.collect { uiState ->
                handleState(uiState, CategoryEnum.BAG.title)
            }
        }

        lifecycleScope.launch {
            viewModel.jeansList.collect { uiState ->
                handleState(uiState, CategoryEnum.JEANS.title)
            }
        }
    }

    private fun handleState(uiState: RecyclerViewWithComplexItemUIState, categoryTitle: String) {
        when (uiState.fetchingStatus) {
            FetchingStatus.INITIAL -> { /* No action needed */
            }

            FetchingStatus.LOADING -> {
            }
            FetchingStatus.SUCCESS -> {
                val newData = uiState.data
                Log.d("viet", "$categoryTitle: success - ${newData.size} items")
                mAdapter.updateContent(categoryTitle, newData)
            }

            FetchingStatus.FAILURE -> Log.d("viet", "$categoryTitle: failure")
        }
    }

    private fun initViews() {
        binding.recyclerViewSections.adapter = mAdapter
        binding.recyclerViewSections.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSections.setItemViewCacheSize(20)
        binding.recyclerViewSections.recycledViewPool.setMaxRecycledViews(0, 20)

        binding.swipeRefreshLayout.setOnRefreshListener {
            mAdapter.setData(arrayListOf())
            mAdapter.setData(generateSections())
            binding.swipeRefreshLayout.isRefreshing = false
        }

        mAdapter.onProductClickListener = { title, product, imageView ->
            Log.d("viet", "title $title")
            Log.d("viet", "product ${product.name}")
            Log.d("viet", "imageView $imageView")

            val navController = findNavController()

            val action = RecyclerViewWithComplexItemFragmentDirections.actionRecyclerViewWithComplexItemFragmentToProductDetailsFragment(product)

            if (!product.thumbnailUrl.isNullOrEmpty()) {
                val extras = FragmentNavigatorExtras(
                    imageView to product.thumbnailUrl
                )
                navController.navigate(action, extras)
            }
        }
    }

    private fun generateSections(): ArrayList<SectionDTO> {
        val sections = arrayListOf<SectionDTO>()
        CategoryEnum.entries.forEach { category ->
            sections.add(
                SectionDTO(
                    category = category,
                    viewMoreAction = { /* Handle view more action */ },
                    items = arrayListOf(),
                    recyclerViewLayoutManager = LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    ),
                    maxItemsCanView = 7,
                    horAdapter = HorizontalAdapter()
                )
            )
        }
        return sections
    }

    override fun onResume() {
        super.onResume()
        mAdapter.setData(generateSections())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}