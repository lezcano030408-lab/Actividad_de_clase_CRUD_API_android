package com.example.crud_dummyjson.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.crud_dummyjson.domain.model.ProductModel
import com.example.crud_dummyjson.ui.composable.ConfirmDeleteDialog
import com.example.crud_dummyjson.ui.section.SeccionDetalleProducto
import com.example.crud_dummyjson.ui.viewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit,
    onEditClick: (ProductModel) -> Unit,
    onDeleteSuccess: () -> Unit,
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val product = uiState.selectedProduct
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog && product != null) {
        ConfirmDeleteDialog(
            productTitle = product.title,
            onConfirm = {
                viewModel.deleteProduct(product)
                showDeleteDialog = false
                onDeleteSuccess()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    if (product != null) {
                        IconButton(onClick = { onEditClick(product) }) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Editar producto"
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Eliminar producto"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (product == null) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontró la información del producto")
            }
        } else {
            SeccionDetalleProducto(
                product = product,
                onEditClick = { onEditClick(product) },
                onDeleteClick = { showDeleteDialog = true },
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}
