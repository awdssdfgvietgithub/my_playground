package com.example.test.features.recycler_view_with_fragment_type

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.ItemPostV1Binding
import com.example.test.databinding.ItemPostV2Binding
import com.example.test.databinding.ItemPostV3Binding
import com.example.test.databinding.ItemPostV4Binding
import com.example.test.features.advanced_recycler_view.PostDTO

class PostListAdapter(private val mViewType: Int) :
    RecyclerView.Adapter<PostListAdapter.ViewHolder>() {
    companion object {
        const val VIEW_ITEM_POST_V1_TYPE = 0
        const val VIEW_ITEM_POST_V2_TYPE = 1
        const val VIEW_ITEM_POST_V3_TYPE = 2
        const val VIEW_ITEM_POST_V4_TYPE = 3
    }

    private var oldData = arrayListOf<PostDTO>()

    fun setData(newData: ArrayList<PostDTO>) {
        val diffUtil = MyDiffUtil(oldData, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldData = ArrayList(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (mViewType) {
            VIEW_ITEM_POST_V1_TYPE -> {
                val binding =
                    ItemPostV1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder.ViewItemPostV1ViewHolder(binding)
            }

            VIEW_ITEM_POST_V2_TYPE -> {
                val binding =
                    ItemPostV2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder.ViewItemPostV2ViewHolder(binding)
            }

            VIEW_ITEM_POST_V3_TYPE -> {
                val binding =
                    ItemPostV3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder.ViewItemPostV3ViewHolder(binding)
            }

            else -> {
                val binding =
                    ItemPostV4Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder.ViewItemPostV4ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = oldData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.ViewItemPostV1ViewHolder -> {
                holder.bind(oldData[position])
            }

            is ViewHolder.ViewItemPostV2ViewHolder -> {
                holder.bind(oldData[position])
            }

            is ViewHolder.ViewItemPostV3ViewHolder -> {
                holder.bind(oldData[position])
            }

            is ViewHolder.ViewItemPostV4ViewHolder -> {
                holder.bind(oldData[position])
            }
        }
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class ViewItemPostV1ViewHolder(private val binding: ItemPostV1Binding) :
            ViewHolder(binding.root) {

            fun bind(item: PostDTO) = with(binding) {
                post = item
                executePendingBindings()
            }
        }

        class ViewItemPostV2ViewHolder(private val binding: ItemPostV2Binding) :
            ViewHolder(binding.root) {

            fun bind(item: PostDTO) = with(binding) {
                post = item
                executePendingBindings()
            }
        }

        class ViewItemPostV3ViewHolder(private val binding: ItemPostV3Binding) :
            ViewHolder(binding.root) {

            fun bind(item: PostDTO) = with(binding) {
                post = item
                executePendingBindings()
            }
        }

        class ViewItemPostV4ViewHolder(private val binding: ItemPostV4Binding) :
            ViewHolder(binding.root) {

            fun bind(item: PostDTO) = with(binding) {
                post = item
                executePendingBindings()
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