package com.example.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mvvm.model.ShipData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ShipHangarViewModel: ViewModel() {
    private val _hangar = MutableStateFlow<List<ShipData>>(emptyList())
    val hangar: StateFlow<List<ShipData>> = _hangar

    /// Adds a ship to the hangar.
    fun addShip(ship: ShipData) {
        _hangar.value += ship
    }

    /// Adds a random ship to the hangar.
    fun addRandomShip() {
        addShip(ShipData.randomShip())
    }

    /// Sends a random ship to battle.
    fun sendShip() {
        val n = (0..<shipCount()).random()
        _hangar.value.drop(n)
    }

    fun ships(): List<ShipData> {
        return hangar.value
    }

    /// Evacuates the hangar and removes all currently stashed ships.
    fun clearHangar() {
        _hangar.value = emptyList()
    }

    /// Returns the total amount of ships in the hangar.
    fun shipCount(): Int {
        return _hangar.value.count()
    }
}