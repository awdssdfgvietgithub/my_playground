package com.example.test.features.recycler_view_with_complex_item

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.test.databinding.ItemBasicBinding
import com.example.test.databinding.ItemBasicShimmerBinding
import com.example.test.databinding.ItemSectionBinding

class SectionsAdapter(private val listener: OnSectionVisibleListener) :
    RecyclerView.Adapter<SectionsAdapter.SectionsViewHolder>() {
    private var data = arrayListOf<SectionDTO>()
    var onProductClickListener: ((title: String, productDTO: ProductDTO, imageView: ImageView) -> Unit)? = null

    interface OnSectionVisibleListener {
        fun onSectionVisible(tag: String)
    }

    fun setData(newData: ArrayList<SectionDTO>) {
        val diffCallback = MyDiffUtils(this.data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.data = newData

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateContent(title: String, newItems: List<ProductDTO>) {
        val index = data.indexOfFirst { it.category.title == title }

        if (index != -1) {
            val section = data[index]

            if (newItems.isNotEmpty()) section.horAdapter.setData(ArrayList(newItems))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionsViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.recyclerViewSection.layoutManager =
            LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
        return SectionsViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SectionsViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item)

        holder.itemView.post {
            listener.onSectionVisible(item.category.queryStr)
        }
    }

    inner class SectionsViewHolder(val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SectionDTO) {
            binding.textViewTitleSection.text = item.category.title
            binding.imageButtonViewMore.setOnClickListener {
                item.viewMoreAction()
            }
            binding.recyclerViewSection.adapter = item.horAdapter
            item.horAdapter.showShimmerEffect()
            item.horAdapter.onItemClickListener = { product, imageView ->
                onProductClickListener?.invoke(item.category.title, product, imageView)
            }
        }
    }

    inner class MyDiffUtils(
        private val oldList: ArrayList<SectionDTO>,
        private val newList: ArrayList<SectionDTO>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].category.title == newList[newItemPosition].category.title
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].category.title == newList[newItemPosition].category.title
        }
    }
}

class HorizontalAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: ArrayList<ProductDTO> = arrayListOf()
    private var isLoading: Boolean = true
    var onItemClickListener: ((ProductDTO, imageView: ImageView) -> Unit)? = null

    enum class ViewType(val type: Int) {
        SHIMMER(0), PRODUCT(1)
    }

    fun setData(newData: ArrayList<ProductDTO>) {
        isLoading = false
        val diffCallback = MyDiffUtils(this.data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.data = newData
        diffResult.dispatchUpdatesTo(this)
    }

    private fun setDataShimmer(newData: ArrayList<ProductDTO>) {
        val diffCallback = MyDiffUtils(this.data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.data = newData
        diffResult.dispatchUpdatesTo(this)
    }

    fun showShimmerEffect() {
        val shimmerData = ArrayList<ProductDTO>().apply { addAll(List(3) { ProductDTO() }) }
        setDataShimmer(shimmerData)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) ViewType.SHIMMER.type else ViewType.PRODUCT.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.SHIMMER.type -> {
                val binding = ItemBasicShimmerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ShimmerViewHolder(binding)
            }

            else -> {
                val binding = ItemBasicBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ProductViewHolder(binding)
            }
        }
    }

    inner class ProductViewHolder(val binding: ItemBasicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ProductDTO) = with(binding) {
            imageProduct.load(item.thumbnailUrl) { crossfade(true) }
            imageProduct.transitionName = item.thumbnailUrl
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(item, imageProduct)
            }

            textViewProductName.text = item.name ?: "-"
            textViewRate.text = item.ratingAverage?.toString() ?: "0"
            textViewQuantitySold.text = item.quantitySold?.text ?: "0"
            textViewPrice.text = item.price?.toString() ?: "0"
            textViewOriginalPrice.text = item.originalPrice?.toString() ?: "0"
            textViewDiscount.text = "-${item.discountRate?.toString() ?: "0"}"
        }
    }

    inner class ShimmerViewHolder(binding: ItemBasicShimmerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return if (isLoading) 3 else data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder && !isLoading) {
            holder.bind(data[position])
        }
    }

    inner class MyDiffUtils(
        private val oldList: ArrayList<ProductDTO>,
        private val newList: ArrayList<ProductDTO>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].price == newList[newItemPosition].price &&
                    oldList[oldItemPosition].discountRate == newList[newItemPosition].discountRate &&
                    oldList[oldItemPosition].thumbnailUrl == newList[newItemPosition].thumbnailUrl &&
                    oldList[oldItemPosition].quantitySold == newList[newItemPosition].quantitySold &&
                    oldList[oldItemPosition].reviewCount == newList[newItemPosition].reviewCount &&
                    oldList[oldItemPosition].ratingAverage == newList[newItemPosition].ratingAverage &&
                    oldList[oldItemPosition].name == newList[newItemPosition].name
        }
    }
}