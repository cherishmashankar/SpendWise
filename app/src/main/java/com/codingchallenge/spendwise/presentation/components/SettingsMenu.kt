package com.codingchallenge.spendwise.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codingchallenge.spendwise.R
import com.codingchallenge.spendwise.presentation.theme.SpendWiseTheme

enum class AppLanguage { ENGLISH, GERMAN }
enum class AppTheme { LIGHT, DARK }

@Composable
fun SettingsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    currentLanguage: AppLanguage,
    currentTheme: AppTheme,
    showChart: Boolean,
    onLanguageChange: (AppLanguage) -> Unit,
    onThemeChange: (AppTheme) -> Unit,
    onToggleChart: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.width(220.dp)
    ) {



        // ---------- Show Chart Toggle ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PieChart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (showChart)
                        stringResource(id = R.string.settings_hide_chart)
                    else
                        stringResource(id = R.string.settings_show_chart),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Switch(
                checked = showChart,
                onCheckedChange = { onToggleChart() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                    checkedTrackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
                )
            )
        }

        DividerLine()
        // ---------- Language Section ----------
        SectionHeader(
            title = stringResource(id = R.string.settings_language),
            icon = Icons.Default.Language,
            color = MaterialTheme.colorScheme.primary
        )

        AppLanguage.entries.forEach { lang ->
            DropdownMenuItem(
                leadingIcon = {
                    if (currentLanguage == lang) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Spacer(modifier = Modifier.width(24.dp))
                    }
                },
                text = {
                    Text(
                        text = when (lang) {
                            AppLanguage.ENGLISH -> stringResource(R.string.language_english)
                            AppLanguage.GERMAN -> stringResource(R.string.language_german)
                        }
                    )
                },
                onClick = {
                    onLanguageChange(lang)
                    onDismiss()
                }
            )
        }

        DividerLine()

        // ---------- Theme Section ----------
        SectionHeader(
            title = stringResource(id = R.string.settings_theme),
            icon = Icons.Default.DarkMode,
            color = MaterialTheme.colorScheme.secondary
        )

        AppTheme.entries.forEach { theme ->
            val icon = when (theme) {
                AppTheme.DARK -> Icons.Default.DarkMode
                AppTheme.LIGHT -> Icons.Default.LightMode
            }

            DropdownMenuItem(
                leadingIcon = {
                    if (currentTheme == theme) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        Icon(
                            icon,
                            contentDescription = theme.name,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                text = {
                    Text(
                        text = when (theme) {
                            AppTheme.LIGHT -> stringResource(R.string.theme_light).lowercase()
                            AppTheme.DARK ->stringResource(R.string.theme_dark).lowercase()
                        }
                    )
                },
                onClick = {
                    onThemeChange(theme)
                    onDismiss()
                }
            )
        }


    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = color,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun DividerLine() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 2.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        thickness = 1.dp
    )
}

@Preview(showBackground = true)
@Composable
private fun SettingsMenuPreview() {
    SpendWiseTheme {
        var expanded by remember { mutableStateOf(true) }

        Box(modifier = Modifier.padding(16.dp)) {
            SettingsMenu(
                expanded = expanded,
                onDismiss = { expanded = false },
                currentLanguage = AppLanguage.ENGLISH,
                currentTheme = AppTheme.DARK,
                showChart = true,
                onLanguageChange = {},
                onThemeChange = {},
                onToggleChart = {}
            )
        }
    }
}
