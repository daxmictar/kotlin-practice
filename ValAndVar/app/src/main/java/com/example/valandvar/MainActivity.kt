package com.example.valandvar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.valandvar.ui.theme.ValAndVarTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ValAndVarTheme {
                //Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                // }
            }
        }
    }
}

data class Item(
    val name: String,
    val price: Double,
    val discount: Double = 1.00,
    // Important for this example, but covered in the mutableStateOf lesson!
    val quantity: MutableIntState = mutableIntStateOf(0),
    // Instead of itemOneRating, we add it to our data class.
    var rating: Double = 5.0,
) {
    fun changeRating(newRating: Double) {
        rating = newRating // Works!
        // name = "Joe" // Won't work, it's immutable (see the val)
    }

    @Composable
    fun ShowOnMarketplace(modifier: Modifier = Modifier, itemIndex: Int) {
        val spaceEvenly = Arrangement.SpaceEvenly
        val centerVertically = Alignment.CenterVertically

        Row(
            horizontalArrangement = spaceEvenly,
            verticalAlignment = centerVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = spaceEvenly,
                verticalAlignment = centerVertically
            ) {
                Box(
                    modifier = Modifier.padding(start = 5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${itemIndex+1}.",
                        fontWeight = FontWeight.Bold
                    )
                }

                val formattedPrice = String.format(Locale.US, "%.2f", price * discount)
                Text(
                    modifier = Modifier.weight(1f),
                    text = $$"$$$formattedPrice | $$name | Qty: $${quantity.intValue}",
                    textAlign = TextAlign.Left

                )

                Button(
                    modifier = Modifier.scale(0.8F).padding(horizontal = 8.dp),
                    onClick = { quantity.intValue -= 1 }
                ) { Text(text = "Buy") }
            }
        }
    }
}

val marketplaceItems: List<Item> = listOf(
    Item(name="Tea Pot", price = 9.99, discount = 0.90, quantity = mutableIntStateOf(20)),
    Item(name="Dining Set", price = 49.99, discount = 1.00, quantity = mutableIntStateOf(10)),
    Item(name="Coffee Mug", price = 4.99, discount = 1.00, quantity = mutableIntStateOf(5)),
    Item(name="Dining Chair", price = 89.99, discount = 1.00, quantity = mutableIntStateOf(1)),
    Item(name="Fork", price = 1.99, discount = 1.00, quantity = mutableIntStateOf(1)),
    Item(name="Spoon", price = 1.99, discount = 1.00, quantity = mutableIntStateOf(1)),
    Item(name="Knife", price = 1.99, discount = 1.00, quantity = mutableIntStateOf(1)),
    Item(name="Napkin", price = 1.99, discount = 1.00, quantity = mutableIntStateOf(1)),
)

@Composable
fun Marketplace() {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            Text(
                text = "E-Commerce Marketplace"
            )
        }
        Spacer(modifier = Modifier.padding(all = 20.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(marketplaceItems) { index, itemVal ->
                Row() {
                    if (itemVal.quantity.intValue > 0) {
                        Box(
                            Modifier.border(width = 1.dp, Color.Gray)
                        ) {
                            itemVal.ShowOnMarketplace(itemIndex = index)
                        }
                    }
                }
                // Text(text="$itemVal")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarketplacePreview() {
    ValAndVarTheme {
        Marketplace()
    }
}