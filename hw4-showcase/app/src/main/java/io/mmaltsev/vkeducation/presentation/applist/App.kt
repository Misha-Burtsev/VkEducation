package io.mmaltsev.vkeducation.presentation.applist

import io.mmaltsev.vkeducation.presentation.appdetails.Category

data class App(
    val name: String,
    val developer: String,
    val category: Category,
    val iconUrl: String,
)
