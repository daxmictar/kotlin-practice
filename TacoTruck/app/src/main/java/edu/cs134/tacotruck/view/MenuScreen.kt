package edu.cs134.tacotruck.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cs134.tacotruck.model.MenuItem
import edu.cs134.tacotruck.model.MenuList
import edu.cs134.tacotruck.viewmodel.TacoTruckMenuViewModel
import edu.cs134.tacotruck.viewmodel.TacoTruckViewModel
import java.util.Locale

@Composable
fun MenuScreen(
    menuViewModel: TacoTruckMenuViewModel,
    tacoTruckViewModel: TacoTruckViewModel,
    modifier: Modifier
) {

    // Taco Truck menu items
    val taco = MenuItem("🌮 Taco", 3.0)
    val burrito = MenuItem("🌯 Burrito", 7.0)
    val guac = MenuItem("🥑 Guacamole", 2.5)
    val horchata = MenuItem("🥛 Horchata", 2.0)
    val quesadilla = MenuItem(name = "🌯 Quesadilla", price = 5.0)

    // when a view is recomposed, it refreshes and adds to the menu, therefore
    // we can just add to the list everytime, rather, we need to replace the
    // original list with the list we want to use.
    menuViewModel.useListAsMenu(
        listOf(
            taco, burrito, guac, horchata, quesadilla
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("🌮 Taco Truck Menu", style = MaterialTheme.typography.headlineMedium)

        LazyColumn() {
            // here, the menu view model is used to display the menu contents
            // and to store each menu item, which is iterated through below
            items(menuViewModel.getMenu()) { menuItem ->
                val name = menuItem.name
                val price = menuItem.price
                val itemText = String.format(Locale.US, "Add %s ($%.2f)", name, price)
                Button(onClick = {
                    // then we add the item to the other view model, which tracks
                    // the item that is added to the order
                    tacoTruckViewModel.addItem(menuItem)
                }) {
                    Text(text = itemText)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}