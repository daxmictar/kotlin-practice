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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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

object PreferenceKeys {
    val LAST_UPDATED = longPreferencesKey("last_updated")
    // todo: add states for current state, monster name, happiness, variant
}

@Composable
fun MyMonsterView(modifier: Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val happyThreshold = 2

    // Collect EVERYTHING here at the top
    val lastUpdated by context.dataStore.data
        .map { it[PreferenceKeys.LAST_UPDATED] ?: 0 }
        .collectAsState(initial = -1)

    // todo: read in monsterState and the rest of our preferences
    // Note: look at lastUpdated for an example of how things are mapped!
    // Note: if you don't remove the hardcoded values below, it won't work!
    val monsterState = 0
    val variant = 1
    val happy = 0
    val monsterName = "Unnamed"

    // Handle initial setup (State 0)
    // We do this inside the View so it can transition to State 1 immediately
    // this is also not visible to the user
    LaunchedEffect(monsterState) {
        if (monsterState == 0) {
            val newVariant = (1..3).random()
            context.dataStore.edit { prefs ->
                // todo: edit state to advance to hatch
                // todo: edit variant to save type
            }
        }
    }

    // HANDLE DECAY ON STARTUP
    LaunchedEffect(lastUpdated) {
        if (lastUpdated > 0L) {
            val currentTime = System.currentTimeMillis()
            val diffInMs = currentTime - lastUpdated
            val secondsPassed = diffInMs / 1000.0

            val decayPerSecond = 0.1
            val pointsToLose = (secondsPassed * decayPerSecond).toInt()

            if (pointsToLose > 0) {
                context.dataStore.edit { prefs ->
                    prefs[PreferenceKeys.LAST_UPDATED] = currentTime
                    // todo: track happiness by subtracting pointsToLose
                }
            }
        }
    }

    Text("Debug State: $monsterState | Variant: $variant")

    // Visible elements go inside this column and
    // switch the contents based ons state
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (monsterState) {
            -1 -> Text("Loading...")
            0 -> Text("The state needs to be updated. Check: \n1. That you have read the state .collectAsState()\n2. That you have edited the state with a new value\n3. That you have removed the hardcoded state")
            1 -> {
                HatchMonster(variant) {
                    scope.launch {
                        context.dataStore.edit {
                            it[PreferenceKeys.LAST_UPDATED] = System.currentTimeMillis()
                            // todo: edit current state to advance to naming
                        }
                    }
                }
            }

            2 -> {
                NameMonster(variant) { name ->
                    // todo: include boilerplate scope and context from HatchMonster
                    // todo: edit name to store the monster name
                    // todo: edit current state to advance to interacting
                }
            }
            3 -> {
                ShowMonster(variant, happy > happyThreshold)
                ShowMonsterHappiness(monsterName, happy > happyThreshold)
                PetMonster {
                    // todo: include boilerplate scope and context from HatchMonster
                    // todo: edit happiness to store the monster's mood
                    // todo: edit last updated to track your interaction
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