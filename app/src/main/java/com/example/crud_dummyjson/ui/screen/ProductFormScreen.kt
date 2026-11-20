package com.example.crud_dummyjson.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.crud_dummyjson.ui.section.SeccionFormularioProducto
import com.example.crud_dummyjson.ui.viewModel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val productToEdit = uiState.selectedProduct

    var title by remember(productToEdit) { mutableStateOf(productToEdit?.title.orEmpty()) }
    var description by remember(productToEdit) { mutableStateOf(productToEdit?.description.orEmpty()) }
    var price by remember(productToEdit) { mutableStateOf(productToEdit?.price?.toString().orEmpty()) }
    var category by remember(productToEdit) { mutableStateOf(productToEdit?.category.orEmpty()) }
    var imageUrl by remember(productToEdit) { mutableStateOf(productToEdit?.imageUrl.orEmpty()) }

    var titleError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (productToEdit != null) "Editar Producto" else "Nuevo Producto"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        SeccionFormularioProducto(
            title = title,
            onTitleChange = {
                title = it
                titleError = false
            },
            titleError = titleError,
            price = price,
            onPriceChange = {
                price = it
                priceError = false
            },
            priceError = priceError,
            category = category,
            onCategoryChange = { category = it },
            imageUrl = imageUrl,
            onImageUrlChange = { imageUrl = it },
            description = description,
            onDescriptionChange = { description = it },
            isEditing = productToEdit != null,
            isSaving = uiState.isSaving,
            onSaveClick = {
                val parsedPrice = price.toDoubleOrNull()
                val isValidTitle = title.isNotBlank()
                val isValidPrice = parsedPrice != null && parsedPrice >= 0

                titleError = !isValidTitle
                priceError = !isValidPrice

                if (isValidTitle && isValidPrice && parsedPrice != null) {
                    val product = ProductModel(
                        id = productToEdit?.id ?: 0,
                        title = title.trim(),
                        description = description.trim(),
                        price = parsedPrice,
                        category = category.trim().ifBlank { "General" },
                        imageUrl = imageUrl.trim()
                    )

                    if (productToEdit != null) {
                        viewModel.updateProduct(product, onSuccess = onSaveSuccess)
                    } else {
                        viewModel.createProduct(product, onSuccess = onSaveSuccess)
                    }
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}
