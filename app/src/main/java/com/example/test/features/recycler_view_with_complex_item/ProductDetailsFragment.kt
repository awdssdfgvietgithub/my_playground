package com.example.test.features.recycler_view_with_complex_item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.api.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.example.test.databinding.FragmentProductDetailsBinding
import com.example.test.features.recycler_view_with_complex_item.utils.setMarginBottom
import com.example.test.features.recycler_view_with_complex_item.utils.setMarginTop
import java.util.concurrent.TimeUnit

class ProductDetailsFragment : DialogFragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        postponeEnterTransition(200, TimeUnit.MILLISECONDS)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.imageButtonBack.setMarginTop(systemBars.top)
            binding.root.setMarginBottom(systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }
        setTransition()
        initViews()
    }

    private fun setTransition() {
        binding.imageProduct.transitionName = args.productData.thumbnailUrl
    }

    private fun initViews() = with(binding) {
        imageProduct.load(args.productData.thumbnailUrl) {
            crossfade(true)
            size(ViewSizeResolver(imageProduct))
            scale(Scale.FILL)
        }
        imageButtonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}