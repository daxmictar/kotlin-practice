package com.example.mymonster

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mymonster.ui.theme.MyMonsterTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "monster_prefs")

class MyMonster : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyMonsterTheme {
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

    suspend fun save(context: Context) {
        val json = Json.encodeToString(this)
        context.dataStore.edit { it[key()] = json }
    }

    suspend fun load(context: Context): MonsterData {
        val json: String? = context.dataStore.data.map { it[key()] }.first()
        return if (json != null) Json.decodeFromString(json) else this
    }
}

@Composable
fun MyMonsterView(modifier: Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val happyThreshold = 2

    val m = MonsterData("A")
    var monster by remember { mutableStateOf(value = m) }

    // Handle initial setup (State 0)
    // We do this inside the View so it can transition to State 1 immediately
    // this is also not visible to the user
    LaunchedEffect(monster.state) {
        if (monster.state == 0) {
            monster = monster.copy(
                variant = (1..3).random(),
                state = 1
            )
        }
    }

    // HANDLE DECAY ON STARTUP
    LaunchedEffect(monster.lastUpdated) {
        if (monster.lastUpdated > 0L) {
            val currentTime = System.currentTimeMillis()
            val diffInMs = currentTime - monster.lastUpdated
            val secondsPassed = diffInMs / 1000.0

            val decayPerSecond = 1
            val pointsToLose = (secondsPassed * decayPerSecond).toInt()

            if (pointsToLose > 0) {
                val currentHappy = monster.happiness
                monster.happiness = currentHappy - pointsToLose
                monster.lastUpdated = currentTime
            }
        }
    }

    Text("Debug State: ${monster.state} | Variant: ${monster.variant} | Happiness: ${monster.happiness}")

    // Visible elements go inside this column and
    // switch the contents based ons state
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (monster.state) {
            -1 -> Text("Loading...")

            1 -> {
                HatchMonster(monster.variant) {
                    scope.launch {
                        monster = monster.copy(state = 2)
                    }
                }
            }

            2 -> {
                NameMonster(monster.variant) { name ->
                    scope.launch {
                        monster = monster.copy(
                            name = name,
                            state = 3
                        )
                    }
                }
            }

            3 -> {
                ShowMonster(monster.variant, monster.happiness > happyThreshold)
                ShowMonsterHappiness(monster.name, monster.happiness > happyThreshold)
                PetMonster {
                    scope.launch {
                        monster = monster.copy(
                            happiness = monster.happiness + 1,
                            lastUpdated = System.currentTimeMillis()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HatchMonster(variant: Int, onHatch: () -> Unit) {

    val imageResource = when (variant) {
        1 -> R.drawable.egg_1
        2 -> R.drawable.egg_2
        else -> R.drawable.egg_3
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
        else -> R.drawable.monster_3normal
    }
    val sadResource = when (variant) {
        1 -> R.drawable.monster_1sad
        2 -> R.drawable.monster_2sad
        else -> R.drawable.monster_3sad
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
    MyMonsterTheme {
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