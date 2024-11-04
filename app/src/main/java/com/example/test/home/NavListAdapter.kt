package com.example.test.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.ItemNavBinding

class NavListAdapter(private var context: Context) :
    RecyclerView.Adapter<NavListAdapter.NavListViewHolder>() {
    private var data = arrayOf<NavDTO>()
    var onItemClick: ((NavDTO) -> Unit)? = null

    fun setData(newData: Array<NavDTO>) {
        val diffCallback = MyDiffUtils(this.data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.data = newData
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavListViewHolder {
        val binding = ItemNavBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NavListViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: NavListViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }

        if (holder.adapterPosition % 2 == 0) {
            holder.itemView.setBackgroundColor(context.getColor(R.color.color_6DE4BA))
        } else {
            holder.itemView.setBackgroundColor(context.getColor(R.color.color_EB9ABE))
        }
    }

    class NavListViewHolder(private val binding: ItemNavBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NavDTO) {
            binding.item = item
        }
    }

    inner class MyDiffUtils(
        private val oldList: Array<NavDTO>,
        private val newList: Array<NavDTO>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].fragmentTag == newList[newItemPosition].fragmentTag
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}