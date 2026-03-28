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

    fun getAlert(): String {
         if (_order.value.count { it.name.contains("Taco") } >= 3) {
            return "\uD83C\uDF36\uFE0F Warning: Spicy overload!";
        }

        return "";
    }

    fun getStatus(): String {
        return when (_order.value.count()) {
            in 0..5 -> "\uD83D\uDFE2 Available"
            in 6..9 -> "\uD83D\uDFE1 Moderate"
            else -> "\uD83D\uDD34 Busy"
        }
    }
}