package com.example.test.features.recycler_view_with_fragment_type

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.ItemSectionBinding

/**
 * https://momen-zaq.medium.com/recyclerview-with-nestedscrollview-best-practices-and-how-to-avoid-it-152fd9ce46d9
 * TODO see above blog
 */
class RecyclerViewWithFragmentTypeAdapter(private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<RecyclerViewWithFragmentTypeAdapter.ViewHolder>() {
    private var oldData = arrayListOf<Fragment>()

    fun setData(newData: ArrayList<Fragment>) {
        val diffUtil = MyDiffUtil(oldData, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldData = ArrayList(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding =
//            ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder.ItemSectionViewHolder(binding, fragmentManager)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)
        return ViewHolder.ItemSectionViewHolder(view, fragmentManager)
    }

    override fun getItemCount(): Int = oldData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolder.ItemSectionViewHolder).bind(oldData[position])
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class ItemSectionViewHolder(
            private val itemView: View,
            private val fragmentManager: FragmentManager
        ) :
            ViewHolder(itemView) {

            fun bind(fragment: Fragment) {
                val fragmentContainer = itemView.findViewById<FrameLayout>(R.id.fragment_container)

                fragmentContainer.post {
                    val transaction = fragmentManager.beginTransaction()
                    val tag = fragment::class.java.simpleName
                    val currentFragment = fragmentManager.findFragmentByTag(tag)

                    if (currentFragment == null) {
                        transaction.replace(fragmentContainer.id, fragment, tag)
                            .commitAllowingStateLoss()
                    }
                }
            }
        }
    }

    inner class MyDiffUtil(
        private val oldList: ArrayList<Fragment>,
        private val newList: ArrayList<Fragment>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldData = oldList[oldItemPosition]
            val newData = newList[newItemPosition]
            return oldData == newData
        }
    }
}

class FragmentRecyclerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentList: List<Fragment>
) : RecyclerView.Adapter<FragmentRecyclerAdapter.FragmentViewHolder>() {

    // ViewHolder chứa một FrameLayout để hiển thị Fragment
    class FragmentViewHolder(val container: FrameLayout) : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentViewHolder {
        // Tạo FrameLayout cho ViewHolder
        val container = FrameLayout(parent.context).apply {
            id = View.generateViewId() // Sử dụng View.generateViewId() để đảm bảo ID là duy nhất
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        // Trả về ViewHolder với FrameLayout vừa tạo
        return FragmentViewHolder(container)
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int) {
        val fragment = fragmentList[position]
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Kiểm tra nếu Fragment chưa được thêm vào, thì thêm vào container
        if (!fragment.isAdded) {
            fragmentTransaction.add(holder.container.id, fragment).commitNow()
        } else {
            fragmentTransaction.show(fragment).commitNow()
        }
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }
}