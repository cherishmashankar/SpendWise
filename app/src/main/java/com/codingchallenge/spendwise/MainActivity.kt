package com.codingchallenge.spendwise

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

import com.codingchallenge.spendwise.presentation.spendwise.SpendWiseViewModel
import com.codingchallenge.spendwise.presentation.spendwise.TransactionScreen
import com.codingchallenge.spendwise.presentation.theme.SpendWiseTheme
import com.codingchallenge.spendwise.utils.AppConstants
import com.codingchallenge.spendwise.utils.updateLocale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val spendWiseViewModel: SpendWiseViewModel = hiltViewModel()
            val uiState by spendWiseViewModel.uiState.collectAsState()
            val themeMode = uiState.themeMode
            val language = uiState.language

            val context = LocalContext.current
            val localizedContext = remember(language) {
                context.updateLocale(language)
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext
            ) {
                SpendWiseTheme(darkTheme = themeMode == AppConstants.THEME_DARK) {
                    TransactionScreen(
                        viewModel = spendWiseViewModel
                    )
                }
            }
        }
    }
}
