package com.codingchallenge.spendwise.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codingchallenge.spendwise.R
import com.codingchallenge.spendwise.presentation.theme.AppDimens
import com.codingchallenge.spendwise.presentation.theme.SpendWiseTheme

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    currentLanguage: AppLanguage,
    currentTheme: AppTheme,
    showChart: Boolean,
    onLanguageChange: (AppLanguage) -> Unit,
    onThemeChange: (AppTheme) -> Unit,
    onToggleChart: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppDimens.PaddingLarge,
                vertical = AppDimens.PaddingSmall
            ),
        shape = RoundedCornerShape(AppDimens.CornerLarge),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_transactions),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_transactions),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            },
            trailingIcon = {
                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.settings_menu),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    SettingsMenu(
                        expanded = menuExpanded,
                        onDismiss = { menuExpanded = false },
                        currentLanguage = currentLanguage,
                        currentTheme = currentTheme,
                        showChart = showChart,
                        onLanguageChange = onLanguageChange,
                        onThemeChange = onThemeChange,
                        onToggleChart = onToggleChart
                    )
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionSearchBarPreview() {
    SpendWiseTheme {
        var text by remember { mutableStateOf("") }

        SearchBar(
            query = text,
            onQueryChange = { text = it },
            currentLanguage = AppLanguage.ENGLISH,
            currentTheme = AppTheme.DARK,
            showChart = true,
            onLanguageChange = {},
            onThemeChange = {},
            onToggleChart = {}
        )
    }
}
