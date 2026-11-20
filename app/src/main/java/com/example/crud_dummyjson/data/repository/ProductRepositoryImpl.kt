package com.example.crud_dummyjson.data.repository

import com.example.crud_dummyjson.data.mapper.toDomain
import com.example.crud_dummyjson.data.mapper.toRequest
import com.example.crud_dummyjson.data.remote.api.ProductService
import com.example.crud_dummyjson.domain.model.ProductModel
import com.example.crud_dummyjson.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductService
) : ProductRepository {

    override suspend fun getProducts(): List<ProductModel> {
        return api.getProducts().products.map { it.toDomain() }
    }

    override suspend fun getProductById(id: Int): ProductModel {
        return api.getProductById(id).toDomain()
    }

    override suspend fun createProduct(product: ProductModel): ProductModel {
        return api.createProduct(product.toRequest()).toDomain()
    }

    override suspend fun updateProduct(product: ProductModel): ProductModel {
        return api.updateProduct(product.id, product.toRequest()).toDomain()
    }

    override suspend fun deleteProduct(id: Int): Boolean {
        api.deleteProduct(id)
        return true
    }
}
