package com.tamuno.unsplash.kmp

import App
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ui.navigation.PhotoScreen

class MainActivity : ComponentActivity() {

    private var deepLinkScreen by mutableStateOf<PhotoScreen?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            App(initialScreen = deepLinkScreen, onDeepLinkHandled = { deepLinkScreen = null })
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val data = intent?.data
        if (data?.scheme == "unsplashkmp" && data.host == "photo") {
            data.getQueryParameter("id")?.let { id ->
                deepLinkScreen = PhotoScreen.DetailScreen(id)
            }
        }
    }
}
