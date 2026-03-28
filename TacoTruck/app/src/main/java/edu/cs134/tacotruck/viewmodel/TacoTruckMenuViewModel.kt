package edu.cs134.tacotruck.viewmodel

import androidx.lifecycle.ViewModel
import edu.cs134.tacotruck.model.MenuItem
import edu.cs134.tacotruck.model.MenuList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.emptyList

// this is new
class TacoTruckMenuViewModel: ViewModel() {
    private val _menu = MutableStateFlow<MenuList>(value = MenuList(emptyList()))

    val menu: StateFlow<MenuList> = _menu;

    fun useListAsMenu(items: List<MenuItem>) {
        menu.value.list = items
    }

    fun getMenu(): List<MenuItem> {
        return _menu.value.list
    }
}