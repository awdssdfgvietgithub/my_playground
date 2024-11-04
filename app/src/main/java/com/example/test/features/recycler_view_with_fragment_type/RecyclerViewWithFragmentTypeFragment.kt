package com.example.test.features.recycler_view_with_fragment_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.databinding.FragmentRecyclerViewWithTypeBinding
import com.example.test.features.recycler_view_with_fragment_type.section_1.Section1Fragment
import com.example.test.features.recycler_view_with_fragment_type.section_10.Section10Fragment
import com.example.test.features.recycler_view_with_fragment_type.section_2.Section2Fragment
import com.example.test.features.recycler_view_with_fragment_type.section_3.Section3Fragment
import com.example.test.features.recycler_view_with_fragment_type.section_4.Section4Fragment
import com.example.test.features.recycler_view_with_fragment_type.section_5.Section5Fragment
import com.example.test.features.recycler_view_with_fragment_type.section_6.Section6Fragment
import com.example.test.features.recycler_view_with_fragment_type.section_7.Section7Fragment
import com.example.test.features.recycler_view_with_fragment_type.section_8.Section8Fragment
import com.example.test.features.recycler_view_with_fragment_type.section_9.Section9Fragment

class RecyclerViewWithFragmentTypeFragment : Fragment() {
    private var _binding: FragmentRecyclerViewWithTypeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: FragmentRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewWithTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLayoutManager = LinearLayoutManager(requireContext())
        mAdapter = FragmentRecyclerAdapter(childFragmentManager, getSections())
        setupRecyclerView()
//        mAdapter.setData(getSections())
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
    }

    private fun getSections() = arrayListOf(
        Section1Fragment(),
        Section2Fragment(),
        Section3Fragment(),
        Section4Fragment(),
        Section5Fragment(),
        Section6Fragment(),
        Section7Fragment(),
        Section8Fragment(),
        Section9Fragment(),
        Section10Fragment(),
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}