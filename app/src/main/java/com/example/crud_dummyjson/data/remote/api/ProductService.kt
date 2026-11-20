package com.example.crud_dummyjson.data.remote.api

import com.example.crud_dummyjson.data.remote.dto.product.PatchProduct
import com.example.crud_dummyjson.data.remote.dto.product.Product
import com.example.crud_dummyjson.data.remote.dto.product.ProductX
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {

    // --- GET (Obtener todos los productos) ---
    @GET("products")
    suspend fun getProducts(): Product

    // --- GET (Obtener producto por ID) ---
    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductX

    // --- POST (Crear producto) ---
    @POST("products/add")
    suspend fun createProduct(
        @Body product: ProductX
    ): ProductX

    // --- PUT (Actualizar producto completo) ---
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: ProductX
    ): ProductX

    // --- PATCH (Actualizar parcialmente un producto) ---
    @PATCH("products/{id}")
    suspend fun patchProductById(
        @Path("id") id: Int,
        @Body product: PatchProduct
    ): ProductX

    // --- DELETE (Eliminar producto) ---
    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int
    ): ProductX
}
