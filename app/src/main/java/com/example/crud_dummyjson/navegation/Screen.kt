package com.example.crud_dummyjson.navegation

sealed class Screen(val route: String) {
    data object ProductsList : Screen("products_list")
    data object ProductForm : Screen("product_form?productId={productId}") {
        fun createRoute(productId: Int? = null): String {
            return if (productId != null) "product_form?productId=$productId" else "product_form"
        }
    }
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int): String {
            return "product_detail/$productId"
        }
    }
}
