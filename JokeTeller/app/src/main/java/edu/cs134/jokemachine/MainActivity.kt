package edu.cs134.jokemachine

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://official-joke-api.appspot.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(JokeApi::class.java)

    private fun fetchJoke(
        onLoadingChange: (Boolean) -> Unit,
        callback: (String) -> Unit
    ) {
        onLoadingChange(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getRandomJoke()
                val joke = response.first()
                val jokeText = "${joke.setup} ... ${joke.punchline}"

                runOnUiThread {
                    onLoadingChange(false)
                    callback(jokeText)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    onLoadingChange(false)
                    callback("Error fetching joke: ${e.message}")
                }
            }
        }
    }

    private fun speakText(text: String) {
        Log.d("TTS", "Speaking: $text")
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private var pendingTextToSpeak: String? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
            ttsReady = true
            pendingTextToSpeak?.let {
                speakText(it)
                pendingTextToSpeak = null
            }
        }
        speakText("Press the button and ask for a joke")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        val engine = tts.defaultEngine
        Log.d("TTS", "Using TTS engine: $engine")

        setContent {
            var jokeText by remember { mutableStateOf("Press the button and ask for a joke!") }
            var loading by remember { mutableStateOf(false) }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (!isGranted) {
                    jokeText = "Microphone permission is required for voice recognition."
                }
            }

            LaunchedEffect(Unit) {
                permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            }

            val speechLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val data = result.data
                if (result.resultCode == RESULT_OK && data != null) {
                    val matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val spokenText = matches?.get(0) ?: ""
                    jokeText = "You said: $spokenText\nFetching joke..."
                    fetchJoke(
                        onLoadingChange = { loading = it },
                        callback = { joke ->
                            jokeText = joke
                            speakText(joke)
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = jokeText, modifier = Modifier.padding(bottom = 24.dp))
                Button(onClick = {
                    if (!SpeechRecognizer.isRecognitionAvailable(this@MainActivity)) return@Button
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    }
                    speechLauncher.launch(intent)
                }) {
                    Text("Talk")
                }
                if (loading) CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
    private var ttsReady = false

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}