package com.example.crud_dummyjson.data.mapper

import com.example.crud_dummyjson.data.remote.dto.product.ProductX
import com.example.crud_dummyjson.domain.model.ProductModel

fun ProductX.toDomain(): ProductModel {
    val resolvedImage = thumbnail?.ifBlank { null } ?: images?.firstOrNull().orEmpty()
    return ProductModel(
        id = id ?: 0,
        title = title.orEmpty(),
        description = description.orEmpty(),
        price = price ?: 0.0,
        category = category.orEmpty(),
        imageUrl = resolvedImage
    )
}

fun ProductModel.toRequest(): ProductX = ProductX(
    id = if (id > 0) id else null,
    title = title,
    description = description,
    price = price,
    category = category,
    thumbnail = imageUrl
)
