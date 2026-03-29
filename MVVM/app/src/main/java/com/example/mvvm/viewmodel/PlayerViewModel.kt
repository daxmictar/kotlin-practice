package com.example.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mvvm.model.Player
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerViewModel : ViewModel() {

    val player = MutableStateFlow(Player())

    fun levelUp() {
        val current = player.value
        player.value = current.copy(
            level = current.level + 1,
            attackPower = current.attackPower + 5,
            health = current.health + 20,
            stamina = current.stamina + 10
        )
    }

    fun takeDamage(amount: Int) {
        val current = player.value
        val newHealth = (current.health - amount).coerceAtLeast(0)
        player.value = current.copy(health = newHealth)
    }

    fun rest() {
        val current = player.value
        player.value = current.copy(stamina = 100)
    }

    fun getHealthStatus(): String {
        return when {
            player.value.health >= 80 -> "💚 Healthy"
            player.value.health >= 40 -> "💛 Injured"
            player.value.health > 0   -> "❤️ Critical"
            else                      -> "💀 Defeated"
        }
    }

    fun getTitle(): String {
        return "${player.value.name} — Level ${player.value.level} Warrior"
    }
}
