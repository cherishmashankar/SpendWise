package com.codingchallenge.spendwise.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import com.codingchallenge.spendwise.presentation.theme.AppDimens


@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    fontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize
) {
    Spacer(modifier = Modifier.height(AppDimens.PaddingLarge))
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = fontSize
        ),
        color = color,
        modifier = modifier
            .padding(
                start = AppDimens.PaddingLarge,
                bottom = AppDimens.PaddingSmall
            )
    )
    Spacer(modifier = Modifier.height(AppDimens.PaddingSmall))
}


