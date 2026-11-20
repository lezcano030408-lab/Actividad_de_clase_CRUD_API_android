package com.example.crud_dummyjson.domain.model

data class ProductModel(
    val id: Int = 0,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String = ""
) {
    val thumbnail: String get() = imageUrl
}
