package com.example.test.home

import com.example.test.R
import com.example.test.utils.Fragments

data class NavDTO(
    val actionId: Int,
    val fragmentTag: String,
    val name: String
) {
    companion object {
        fun mockData() = arrayOf(
            NavDTO(
                R.id.action_homeFragment_to_MPHorizontalBarChartFragment,
                Fragments.MP_HORIZONTAL_BAR_CHART.fragmentTag,
                Fragments.MP_HORIZONTAL_BAR_CHART.fragmentName
            ),
            NavDTO(
                R.id.action_homeFragment_to_advancedRecyclerViewFragment,
                Fragments.ADVANCED_RECYCLER_VIEW.fragmentTag,
                Fragments.ADVANCED_RECYCLER_VIEW.fragmentName
            ),
            NavDTO(
                R.id.action_homeFragment_to_recyclerViewWithComplexItemFragment,
                Fragments.RECYCLER_VIEW_WITH_COMPLEX_ITEM.fragmentTag,
                Fragments.RECYCLER_VIEW_WITH_COMPLEX_ITEM.fragmentName
            ),
            NavDTO(
                R.id.action_homeFragment_to_livenessFragment,
                Fragments.LIVENESS.fragmentTag,
                Fragments.LIVENESS.fragmentName
            ),
            NavDTO(
                R.id.action_homeFragment_to_stringErrorsMappingUIFragment,
                Fragments.STRING_ERRORS_MAPPING_UI.fragmentTag,
                Fragments.STRING_ERRORS_MAPPING_UI.fragmentName
            )
        )
    }
}
