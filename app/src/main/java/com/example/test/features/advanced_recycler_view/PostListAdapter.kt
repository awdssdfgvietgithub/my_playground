package com.example.test.features.advanced_recycler_view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ItemLoadingBinding
import com.example.test.databinding.ItemPostV1Binding

class PostListAdapter(
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    private var isLoading = false

    private var oldData = arrayListOf<PostDTO>()

    fun setData(newData: ArrayList<PostDTO>) {
        val filteredData = newData.filter { it.id != -1 }
            .toCollection(ArrayList()) // Exclude the loading item if present
        val diffUtil = MyDiffUtil(oldData, filteredData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldData = ArrayList(filteredData)
        diffResult.dispatchUpdatesTo(this)
    }

    fun showLoading() {
        if (!isLoading && oldData.isNotEmpty()) {
            isLoading = true
            oldData.add(PostDTO(-1, -1, "", ""))
            notifyItemInserted(oldData.size - 1)
        }
    }

    fun hideLoading() {
        if (isLoading && oldData.isNotEmpty()) {
            isLoading = false
            val position = oldData.size - 1
            if (oldData[position].id == -1) {
                oldData.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (oldData[position].id == -1) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_LOADING) {
            val binding =
                ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder.LoadingViewHolder(binding)
        } else {
            val binding =
                ItemPostV1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder.ItemViewHolder(binding, onItemClick)
        }
    }

    override fun getItemCount(): Int = oldData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ViewHolder.LoadingViewHolder) {
            return
        }
        (holder as ViewHolder.ItemViewHolder).bind(oldData[position])
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class ItemViewHolder(private val binding: ItemPostV1Binding, private val onItemClick: (position: Int) -> Unit) :
            ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    adapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                        onItemClick(position)
                    }
                }
            }

            fun bind(item: PostDTO) = with(binding) {
                post = item
                executePendingBindings()
            }
        }

        class LoadingViewHolder(private val binding: ItemLoadingBinding) :
            ViewHolder(binding.root) {

            fun bind() = with(binding) {
            }
        }
    }

    inner class MyDiffUtil(
        private val oldList: ArrayList<PostDTO>,
        private val newList: ArrayList<PostDTO>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldData = oldList[oldItemPosition]
            val newData = newList[newItemPosition]
            return oldData.id == newData.id &&
                    oldData.body == newData.body &&
                    oldData.userId == newData.userId &&
                    oldData.title == newData.title
        }
    }
}