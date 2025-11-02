package com.codingchallenge.spendwise.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingchallenge.spendwise.domain.model.TransactionType
import java.util.*

@Composable
fun AmountInputField(
    amount: String,
    onAmountChange: (String) -> Unit,
    type: TransactionType,
    modifier: Modifier = Modifier
) {
    val incomeColor = Color(0xFF4CAF50)
    val expenseColor = Color(0xFFE53935)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = { input ->
                if (input.all { it.isDigit() || it == ',' || it == '.' }) {
                    onAmountChange(input)
                }
            },
            label = { Text("Amount (€)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = if (type == TransactionType.INCOME) incomeColor else expenseColor,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Safely converts an input string to a Double based on the locale.
 * - German ("de") uses ',' for decimals
 * - English ("en") uses '.'
 */
fun String.toLocalizedDouble(locale: Locale): Double {
    if (this.isBlank()) return 0.0
    val normalized = when (locale.language) {
        "de" -> this.replace(".", "").replace(',', '.')
        else -> this.replace(",", "")
    }
    return normalized.toDoubleOrNull() ?: 0.0
}
