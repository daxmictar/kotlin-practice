package edu.cs134.tacotruck.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import edu.cs134.tacotruck.model.MenuItem

class TacoTruckViewModel : ViewModel() {

    private val _order = MutableStateFlow<List<MenuItem>>(emptyList())
    val order: StateFlow<List<MenuItem>> = _order

    fun addItem(item: MenuItem) {
        _order.value = _order.value + item
    }

    fun clearOrder() {
        _order.value = emptyList()
    }

    fun getTotal(): Double {
        return _order.value.sumOf { it.price }
    }
}