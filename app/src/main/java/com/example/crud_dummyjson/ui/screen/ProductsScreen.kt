package com.example.crud_dummyjson.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.crud_dummyjson.domain.model.ProductModel
import com.example.crud_dummyjson.ui.composable.ConfirmDeleteDialog
import com.example.crud_dummyjson.ui.composable.LoadingState
import com.example.crud_dummyjson.ui.composable.MessageWithActionState
import com.example.crud_dummyjson.ui.section.ProductListSection
import com.example.crud_dummyjson.ui.viewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onAddProductClick: () -> Unit,
    onProductClick: (ProductModel) -> Unit,
    onEditProductClick: (ProductModel) -> Unit,
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var productToDelete by remember { mutableStateOf<ProductModel?>(null) }

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        val message = uiState.errorMessage ?: uiState.successMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessages()
        }
    }

    productToDelete?.let { product ->
        ConfirmDeleteDialog(
            productTitle = product.title,
            onConfirm = {
                viewModel.deleteProduct(product)
                productToDelete = null
            },
            onDismiss = { productToDelete = null }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Productos") },
                actions = {
                    IconButton(onClick = { viewModel.loadProducts() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar lista"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar producto"
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            when {
                uiState.isLoading -> LoadingState()

                uiState.errorMessage != null && uiState.products.isEmpty() -> {
                    MessageWithActionState(
                        message = uiState.errorMessage ?: "Ocurrió un error",
                        actionLabel = "Reintentar",
                        onActionClick = { viewModel.loadProducts() }
                    )
                }

                uiState.products.isEmpty() -> {
                    MessageWithActionState(
                        message = "No hay productos disponibles",
                        actionLabel = "Cargar",
                        onActionClick = { viewModel.loadProducts() }
                    )
                }

                else -> {
                    ProductListSection(
                        products = uiState.products,
                        onProductClick = { product ->
                            viewModel.selectProduct(product)
                            onProductClick(product)
                        },
                        onEditClick = { product ->
                            viewModel.selectProduct(product)
                            onEditProductClick(product)
                        },
                        onDeleteClick = { product ->
                            productToDelete = product
                        }
                    )
                }
            }
        }
    }
}
