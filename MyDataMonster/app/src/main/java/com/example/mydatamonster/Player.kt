package com.example.mydatamonster

class Enemy(
    var health: Int = 100,
    var defense: Int = 10,
) {
}

data class Player(
    val name: String = "John Doe",
    var level: Int = 1,
    var health: Int = 100,
    var stamina: Int = 100,
    var attackPower: Int = 10,
) {
    fun attack(enemy: Enemy): Int {
        val roll = (1..20).random()
        return if (roll >= enemy.defense) enemy.health - attackPower else 0
    }

    override fun toString(): String {
        return "Name: $name\n" +
               "Level: $level\n" +
               "Health: $health\n" +
               "Stamina: $stamina\n" +
               "Power: $attackPower\n"
    }
}