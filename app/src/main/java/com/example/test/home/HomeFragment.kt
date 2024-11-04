package com.example.test.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.R
import com.example.test.databinding.FragmentHomeBinding
import com.example.test.features.mp_horizontal_bar_chart.MPHorizontalBarChartFragment

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val navListAdapter: NavListAdapter by lazy { NavListAdapter(requireContext()) }
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(requireContext())
        displayRVNavList()
    }

    private fun displayRVNavList() {
        navListAdapter.setData(NavDTO.mockData())
        binding.rvNavList.apply {
            layoutManager = linearLayoutManager
            adapter = navListAdapter
        }
        navListAdapter.onItemClick = { item ->
            val navController = findNavController()
            if (navController.currentDestination?.id == R.id.homeFragment) {
                    navController.navigate(item.actionId)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}