package com.example.mvvm.model

enum class ShipCategory {
    Fighter,
    Battleship,
    Freighter,
    Racer;

    companion object {
        data class ShipCategoryValues(val size: Int, val seats: Int, val weapons: Int)

        fun categoryToValues(category: ShipCategory): ShipCategoryValues {
            return when (category) {
                Fighter -> ShipCategoryValues(20, 1, 4)
                Battleship -> ShipCategoryValues(50, 30, 15)
                Racer -> ShipCategoryValues(10, 1, 1)
                Freighter -> ShipCategoryValues(40, 10, 4)
            }
        }
    }
}

data class ShipData(
    val name: String = "X-wing",
    val size: Int = 20,
    var numSeats: Int = 1,
    var numWeapons: Int = 4,
    val category: ShipCategory = ShipCategory.Fighter,
) {
    companion object {
        fun randomShip(): ShipData {
            val randomCategory = ShipCategory.entries.toTypedArray().random()
            val (size, seats, weapons) = ShipCategory.categoryToValues(randomCategory)
            return ShipData(
                name = "New Ship",
                size = size,
                numSeats = seats,
                numWeapons = weapons,
                category = randomCategory
            )
        }
    }

    override fun toString(): String{
        return "$name: $category\nthe number of seats: $numSeats\nWeapons: $numWeapons";
    }
}
