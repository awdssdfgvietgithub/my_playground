package com.example.test.features.recycler_view_with_complex_item

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.FragmentRecyclerViewWithComplexItemBinding
import com.example.test.databinding.ItemBasicBinding
import com.example.test.databinding.ItemNavBinding
import com.example.test.databinding.ItemSectionBinding
import com.example.test.home.NavDTO
import com.example.test.home.NavListAdapter

class RecyclerViewWithComplexItemFragment : Fragment() {
    private var _binding: FragmentRecyclerViewWithComplexItemBinding? = null
    private val binding get() = _binding!!

    private val mAdapter by lazy { SectionsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerViewWithComplexItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.recyclerViewSections.adapter = mAdapter
        binding.recyclerViewSections.layoutManager = LinearLayoutManager(requireContext())
        mAdapter.setData(generateSections())
    }

    private fun generateSections(): ArrayList<SectionDTO> {
        val sections = arrayListOf<SectionDTO>()
        for (i in 0..10) {
            sections.add(
                SectionDTO(
                    tag = "section_$i",
                    title = "Section $i",
                    viewMoreAction = { /* Handle view more action */ },
                    items = arrayListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7"),
                    recyclerViewLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                    maxItemsCanView = 7
                )
            )
        }
        return sections
    }
}

data class SectionDTO(
    var tag: String,
    var title: String,
    var viewMoreAction: () -> Unit,
    val items: ArrayList<String> = arrayListOf(),
    var recyclerViewLayoutManager: RecyclerView.LayoutManager,
    var maxItemsCanView: Int = 7,
)

class SectionsAdapter() : RecyclerView.Adapter<SectionsAdapter.SectionsViewHolder>() {
    private var data = arrayListOf<SectionDTO>()

    fun setData(newData: ArrayList<SectionDTO>) {
        val diffCallback = MyDiffUtils(this.data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.data = newData
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionsViewHolder {
        val binding = ItemSectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SectionsViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SectionsViewHolder, position: Int) {
        val item = data[position]
        Log.d("viet", "SectionsAdapter: $position - ${item.tag}")

        holder.bind(item)
        if (holder.adapterPosition % 2 == 0) {
            holder.itemView.setBackgroundColor(holder.binding.root.context.getColor(R.color.color_6DE4BA))
        } else {
            holder.itemView.setBackgroundColor(holder.binding.root.context.getColor(R.color.color_EB9ABE))
        }
    }

    inner class SectionsViewHolder(val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SectionDTO) {
            binding.textViewTitleSection.text = item.title
            binding.imageButtonViewMore.setOnClickListener {
                item.viewMoreAction()
            }
            val horAdapter = HorizontalAdapter()
            binding.recyclerViewSection.adapter = HorizontalAdapter()
            binding.recyclerViewSection.layoutManager = item.recyclerViewLayoutManager
            horAdapter.setData(item.items)
        }
    }

    inner class MyDiffUtils(
        private val oldList: ArrayList<SectionDTO>,
        private val newList: ArrayList<SectionDTO>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].tag == newList[newItemPosition].tag
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].tag == newList[newItemPosition].tag
        }
    }

    inner class HorizontalAdapter() : RecyclerView.Adapter<HorizontalAdapter.ViewHolder>() {
        private var data = arrayListOf<String>()

        fun setData(newData: ArrayList<String>) {
//            val diffCallback = MyDiffUtils(this.data, newData)
//            val diffResult = DiffUtil.calculateDiff(diffCallback)
//            this.data = newData
//            diffResult.dispatchUpdatesTo(this)
            this.data = newData
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemBasicBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        inner class ViewHolder(val binding: ItemBasicBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(item: String) {
                binding.textViewBasic.text = item
            }
        }

        override fun getItemCount(): Int = this.data.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = this.data[position]
            Log.d("viet", "HorizontalAdapter: $position - ${item}")
            holder.bind(item)
        }

        inner class MyDiffUtils(
            private val oldList: ArrayList<String>,
            private val newList: ArrayList<String>
        ) : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        }
    }
}