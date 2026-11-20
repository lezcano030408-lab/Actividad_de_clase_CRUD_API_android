package com.example.crud_dummyjson.navegation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.crud_dummyjson.ui.screen.ProductDetailScreen
import com.example.crud_dummyjson.ui.screen.ProductFormScreen
import com.example.crud_dummyjson.ui.screen.ProductsScreen
import com.example.crud_dummyjson.ui.viewModel.ProductViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: ProductViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ProductsList.route,
        modifier = modifier
    ) {
        composable(route = Screen.ProductsList.route) {
            ProductsScreen(
                onAddProductClick = {
                    viewModel.clearSelection()
                    navController.navigate(Screen.ProductForm.createRoute())
                },
                onProductClick = { product ->
                    viewModel.selectProduct(product)
                    navController.navigate(Screen.ProductDetail.createRoute(product.id))
                },
                onEditProductClick = { product ->
                    viewModel.selectProduct(product)
                    navController.navigate(Screen.ProductForm.createRoute(product.id))
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.ProductForm.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: -1
            if (productId > 0) {
                viewModel.selectProductById(productId)
            }
            ProductFormScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveSuccess = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: -1
            if (productId > 0) {
                viewModel.selectProductById(productId)
            }
            ProductDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { product ->
                    viewModel.selectProduct(product)
                    navController.navigate(Screen.ProductForm.createRoute(product.id))
                },
                onDeleteSuccess = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
    }
}
