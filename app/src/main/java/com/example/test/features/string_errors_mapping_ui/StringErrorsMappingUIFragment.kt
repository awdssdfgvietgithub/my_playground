package com.example.test.features.string_errors_mapping_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.test.databinding.FragmentStringErrorsMappingUIBinding
import com.google.android.material.snackbar.Snackbar


class StringErrorsMappingUIFragment : Fragment() {
    private var _binding: FragmentStringErrorsMappingUIBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStringErrorsMappingUIBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        editTextErrorCode.requestFocus()
        ViewCompat.getWindowInsetsController(requireView())?.show(WindowInsetsCompat.Type.ime())

        buttonExecute.setOnClickListener {
            val errorCode = editTextErrorCode.text.toString()
            if (errorCode.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    "Please input your error code!",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val isOMS = checkBoxOms.isChecked
            val stringArg = editTextStringArg.text.toString()
            val intArg = editTextIntArg.text.toString().toIntOrNull()
            val floatArg = editTextFloatArg.text.toString().toFloatOrNull()

            val errorString =
                requireContext().getErrorStringFOByCode(
                    errorCode,
                    isOMS,
                    stringArg.ifEmpty { null },
                    intArg,
                    floatArg
                )
            textViewResult.text = errorString
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}