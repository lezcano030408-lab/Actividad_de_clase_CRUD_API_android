package com.example.crud_dummyjson.data.remote.dto.product

data class ProductListResponse(
    val product: List<Product> = emptyList()
)
