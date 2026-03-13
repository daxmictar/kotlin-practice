package com.example.mydatamonster

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mydatamonster.ui.theme.MyDataMonsterTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "monster_prefs")

class MyDataMonster : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDataMonsterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    innerPadding ->
                    MyMonsterView(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Serializable
data class MonsterData (
    val id: String,
    var variant: Int = 0,
    var name: String = "",
    var state: Int = 0,
    var happiness: Int = 0,
    var lastUpdated: Long = 0 ,
) {
    fun key() = stringPreferencesKey("monster_$id")

    fun calculateDecay() {
        if (this.lastUpdated > 0L) {
            val currentTime = System.currentTimeMillis()
            val diffInMs = currentTime - this.lastUpdated
            val secondsPassed = diffInMs / 1000.0

            val decayPerSecond = 1
            val pointsToLose = (secondsPassed * decayPerSecond).toInt()

            if (pointsToLose > 0) {
                val currentHappy = this.happiness
                val newHappy = currentHappy - pointsToLose
                this.happiness = if (newHappy < 0) 0 else newHappy
                this.lastUpdated = currentTime
            }
        }
    }

    suspend fun save(context: Context) {
        val json = Json.encodeToString(this)
        context.dataStore.edit { it[key()] = json }
    }

    suspend fun load(context: Context): MonsterData {
        val json: String? = context.dataStore.data.map { it[key()] }.first()
        return if (json != null) Json.decodeFromString(json) else this
    }
}

class MonsterManager(
    val context: Context,
    val scope: CoroutineScope
) {
    private val monsterList = mutableListOf<MonsterData>(
        MonsterData("A"),
        MonsterData("B"),
        MonsterData("C"),
        MonsterData("D"),
    )

    var activeIndex by mutableIntStateOf(value = 0)
    val activeMonster = mutableStateOf(monsterList[activeIndex])

    fun swap() {
        activeIndex = (activeIndex + 1) % monsterList.size
        // triggers a recompose
        forceRecompose(monsterList[activeIndex])
    }

    fun loadAll() {
        scope.launch {
            for (i in monsterList.indices) {
                monsterList[i] = monsterList[i].load(context)
            }
            forceRecompose(monsterList[activeIndex])
        }
    }

    fun update(update: MonsterData) {
        update.lastUpdated = System.currentTimeMillis()
        forceRecompose(update)
        scope.launch {
            monsterList[activeIndex] = update
            monsterList[activeIndex].save(context)
        }
    }

    fun forceRecompose(update: MonsterData) {
        activeMonster.value = update
    }

    fun resetMonsters() {
        for (i in monsterList.indices) {
            monsterList[i] = MonsterData(id=monsterList[i].id)
        }
        forceRecompose(monsterList[activeIndex])
    }
}

@Composable
fun MyMonsterView(modifier: Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val happyThreshold = 2

    val manager by remember { mutableStateOf(MonsterManager(context, scope)) }

    LaunchedEffect(Unit) { manager.loadAll() }

    val monster by manager.activeMonster

    LaunchedEffect(monster.lastUpdated) {
        monster.calculateDecay()
    }

    Text("Debug: $monster")
    // Text("Debug State: ${monster.state} | Variant: ${monster.variant} | Happiness: ${monster.happiness}")

    // Visible elements go inside this column and
    // switch the contents based ons state
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (monster.state) {
            -1 -> Text("Loading...")

            0 -> {
                val update = monster.copy(
                    variant = (1..4).random(),
                    state = 1
                )
                manager.update(update)
            }

            1 -> {
                HatchMonster(monster.variant) {
                    val update = monster.copy(
                        state = 2
                    )
                    manager.update(update)
                }
            }

            2 -> {
                NameMonster(monster.variant) { name ->
                    val update = monster.copy(
                        name = name,
                        state = 3
                    )
                    manager.update(update)
                }
            }

            3 -> {
                ShowMonster(monster.variant, monster.happiness > happyThreshold)
                ShowMonsterHappiness(monster.name, monster.happiness > happyThreshold)
                PetMonster {
                    val update = monster.copy(
                        happiness = monster.happiness + 1,
                    )
                    manager.update(update)
                }
            }
        }

        Button(onClick = { manager.swap() }) {
            Text("Swap Monster")
        }

        Button(onClick = { manager.resetMonsters() }) {
            Text("Reset Monsters")
        }
    }
}

@Composable
fun HatchMonster(variant: Int, onHatch: () -> Unit) {
    val imageResource = when (variant) {
        1 -> R.drawable.egg_1
        2 -> R.drawable.egg_2
        3 -> R.drawable.egg_3
        else ->  R.drawable.egg_2
    }

    Column(modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("You have a new egg!")
        Image(painterResource(imageResource),
            "Egg of variant $variant")
        Button(onClick = onHatch) {
            Text("Hatch")
        }
    }
}

@Composable
fun NameMonster(variant: Int, onName: (name: String) -> Unit) {
    ShowMonster(variant, true)

    // Local state for what the user is currently typing
    var textInput by remember { mutableStateOf("") }

    Text("What will you name your monster?")

    OutlinedTextField(
        value = textInput,
        onValueChange = { textInput = it },
        label = { Text("Monster Name") },
        modifier = Modifier.fillMaxWidth()
    )

    Button(
        enabled = textInput.isNotBlank(), // Don't allow empty names
        onClick = { onName(textInput) }
    ) {
        Text("Confirm Name")
    }
}


@Composable
fun ShowMonster(variant: Int, happy: Boolean) {
    val happyResource = when (variant) {
        1 -> R.drawable.monster_1normal
        2 -> R.drawable.monster_2normal
        3 -> R.drawable.monster_3normal
        else -> R.drawable.monster4_normal
    }
    val sadResource = when (variant) {
        1 -> R.drawable.monster_1sad
        2 -> R.drawable.monster_2sad
        3 -> R.drawable.monster_3sad
        else -> R.drawable.monster4_sad
    }

    Image(painterResource(if (happy) happyResource else sadResource),
        "Monster of variant $variant")
}

@Composable
fun ShowMonsterHappiness(name: String, happy: Boolean) {
    val happiness = if (happy) "happy" else "sad"
    Text("$name is $happiness")
}

@Composable
fun PetMonster(onPet: () -> Unit) {
    Button(onClick = onPet) {
        Text("Pet")
    }
}

@Preview
@Composable
fun PreviewUIElements() {
    MyDataMonsterTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) { }
        Column (modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            val variant = (1..3).random()
            HatchMonster(variant) { }
            NameMonster(variant) { name -> {} }
            ShowMonster(variant, false)
            ShowMonsterHappiness("Monster", false)
            PetMonster() {}
        }
    }
}