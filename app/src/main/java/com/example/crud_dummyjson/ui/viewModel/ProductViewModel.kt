package com.example.crud_dummyjson.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud_dummyjson.domain.model.ProductModel
import com.example.crud_dummyjson.domain.repository.ProductRepository
import com.example.crud_dummyjson.ui.state.ProductUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUIState())
    val uiState: StateFlow<ProductUIState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { repository.getProducts() }
                .onSuccess { list ->
                    _uiState.update { it.copy(isLoading = false, products = list) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "No se pudieron cargar los productos"
                        )
                    }
                }
        }
    }

    fun selectProduct(product: ProductModel?) {
        _uiState.update { it.copy(selectedProduct = product) }
    }

    fun selectProductById(id: Int) {
        viewModelScope.launch {
            val existing = _uiState.value.products.find { it.id == id }
            if (existing != null) {
                _uiState.update { it.copy(selectedProduct = existing) }
            } else {
                _uiState.update { it.copy(isLoading = true) }
                runCatching { repository.getProductById(id) }
                    .onSuccess { product ->
                        _uiState.update { it.copy(isLoading = false, selectedProduct = product) }
                    }
                    .onFailure { e ->
                        _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                    }
            }
        }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedProduct = null) }
    }

    fun createProduct(product: ProductModel, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            runCatching { repository.createProduct(product) }
                .onSuccess { created ->
                    _uiState.update { state ->
                        state.copy(
                            isSaving = false,
                            products = listOf(created) + state.products,
                            selectedProduct = null,
                            successMessage = "Producto agregado correctamente"
                        )
                    }
                    onSuccess()
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "No se pudo agregar el producto"
                        )
                    }
                }
        }
    }

    fun updateProduct(product: ProductModel, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            runCatching { repository.updateProduct(product) }
                .onSuccess { updated ->
                    _uiState.update { state ->
                        state.copy(
                            isSaving = false,
                            products = state.products.map { if (it.id == updated.id) updated else it },
                            selectedProduct = null,
                            successMessage = "Producto actualizado correctamente"
                        )
                    }
                    onSuccess()
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "No se pudo actualizar el producto"
                        )
                    }
                }
        }
    }

    fun deleteProduct(product: ProductModel) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }
            runCatching { repository.deleteProduct(product.id) }
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isSaving = false,
                            products = state.products.filterNot { it.id == product.id },
                            selectedProduct = null,
                            successMessage = "Producto eliminado correctamente"
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "No se pudo eliminar el producto"
                        )
                    }
                }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
