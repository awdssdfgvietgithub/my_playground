package com.example.test.utils

enum class Fragments(val fragmentTag: String, val fragmentName: String) {
    HOME("HOME", "home fragment"),
    MP_HORIZONTAL_BAR_CHART("MP_HORIZONTAL_BAR_CHART", "MP horizontal bar chart fragment"),
    ADVANCED_RECYCLER_VIEW("ADVANCED_RECYCLER_VIEW", "Advanced recycler view fragment"),
    RECYCLER_VIEW_WITH_FRAGMENT_TYPE("RECYCLER_VIEW_WITH_FRAGMENT_TYPE", "RecyclerView with fragment"),
    RECYCLER_VIEW_WITH_COMPLEX_ITEM("RECYCLER_VIEW_WITH_COMPLEX_ITEM", "RecyclerView with complex item fragment"),
    LIVENESS("LIVENESS", "Liveness fragment"),
    STRING_ERRORS_MAPPING_UI("STRING_ERRORS_MAPPING_UI", "String errors mapping UI"),
}

enum class FetchingStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    FAILURE
}