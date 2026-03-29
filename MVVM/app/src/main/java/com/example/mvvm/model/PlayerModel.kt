package com.example.mvvm.model

data class Player(
    val name: String = "Warrior",
    var level: Int = 1,
    var health: Int = 100,
    var stamina: Int = 100,
    var attackPower: Int = 10,
) {
    override fun toString(): String {
        return "Name: $name\n" +
               "Level: $level\n" +
               "Health: $health\n" +
               "Stamina: $stamina\n" +
               "Power: $attackPower\n"
    }
}