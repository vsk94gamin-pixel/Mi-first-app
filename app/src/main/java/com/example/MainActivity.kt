package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.MainScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.TakaFlowTheme
import com.example.ui.viewmodel.ScreenDestination
import com.example.ui.viewmodel.TakaFlowViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: TakaFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TakaFlowTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val user by viewModel.currentUser.collectAsState()

                Crossfade(
                    targetState = currentScreen,
                    modifier = Modifier.fillMaxSize(),
                    label = "screen_crossfade"
                ) { screen ->
                    when (screen) {
                        is ScreenDestination.Splash -> {
                            SplashScreen(
                                onSplashFinished = {
                                    if (user != null) {
                                        viewModel.navigateTo(ScreenDestination.Main)
                                    } else {
                                        viewModel.navigateTo(ScreenDestination.Auth)
                                    }
                                }
                            )
                        }
                        is ScreenDestination.Auth -> {
                            AuthScreen(viewModel = viewModel)
                        }
                        is ScreenDestination.Main -> {
                            MainScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

