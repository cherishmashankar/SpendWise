package com.codingchallenge.spendwise.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingchallenge.spendwise.R
import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.domain.model.TransactionType
import com.codingchallenge.spendwise.presentation.theme.AppDimens
import com.codingchallenge.spendwise.presentation.theme.SpendWiseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionDialog(
    transaction: Transaction? = null,
    onDismiss: () -> Unit,
    onSave: (Transaction) -> Unit
) {
    var title by remember { mutableStateOf(transaction?.title ?: "") }
    var amount by remember { mutableStateOf(transaction?.amount?.toString() ?: "") }
    var note by remember { mutableStateOf(transaction?.notes ?: "") }
    var type by remember { mutableStateOf(transaction?.type ?: TransactionType.INCOME) }

    val focusManager = LocalFocusManager.current
    val isEditing = transaction != null
    val isValid by remember {
        derivedStateOf {
            title.isNotBlank() && (amount.toDoubleOrNull()?.let { it > 0 } == true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
/*            Text(
                text = if (isEditing)
                    stringResource(R.string.edit_transaction)
                else
                    stringResource(R.string.add_transaction),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )*/
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingMedium)
            ) {
                // --- Title Input ---
                BasicTextField(
                    value = title,
                    onValueChange = { newText -> title = newText.replace("\n", "") },
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    textStyle = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppDimens.PaddingSmall),
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text(
                                text = stringResource(R.string.transaction_title_label),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        innerTextField()
                    }
                )
                Spacer(modifier = Modifier.height(AppDimens.PaddingSmall))


                // --- Type and Amount Row ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var expanded by remember { mutableStateOf(false) }

                    // Dropdown Menu
                    Box(modifier = Modifier.weight(1f)) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = if (type == TransactionType.INCOME)
                                    stringResource(R.string.filter_income)
                                else
                                    stringResource(R.string.filter_expenses),
                                onValueChange = {},
                                label = { Text(stringResource(R.string.transaction_type_label)) },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.filter_income)) },
                                    onClick = {
                                        type = TransactionType.INCOME
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.filter_expenses)) },
                                    onClick = {
                                        type = TransactionType.EXPENSE
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Amount Field
                    OutlinedTextField(
                        value = amount,
                        onValueChange = {
                            if (it.all { ch -> ch.isDigit() || ch == '.' || ch == ',' })
                                amount = it
                        },
                        label = { Text(stringResource(R.string.transaction_amount_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                // --- Note Field ---
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(stringResource(R.string.transaction_note_label)) },
                    singleLine = false,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                    val transactionToSave = Transaction(
                        id = transaction?.id ?: 0,
                        title = title.trim(),
                        amount = parsedAmount,
                        type = type,
                        category = transaction?.category,
                        notes = note.trim(),
                        timestamp = transaction?.timestamp ?: System.currentTimeMillis(),
                        lastModified = System.currentTimeMillis()
                    )
                    onSave(transactionToSave)
                    onDismiss()
                },
                enabled = isValid
            ) {
                Text(
                    if (isEditing) stringResource(R.string.save) else stringResource(R.string.add),
                    fontSize = 16.sp,
                    color = if (isValid) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AddTransactionDialogPreview() {
    SpendWiseTheme {
        AddEditTransactionDialog(
            transaction = null,
            onDismiss = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditTransactionDialogPreview() {
    SpendWiseTheme {
        AddEditTransactionDialog(
            transaction = Transaction(
                id = 1,
                title = "Gehalt",
                amount = 1200.0,
                type = TransactionType.INCOME,
                notes = "Monatliches Gehalt",
                category = "Einkommen",
                timestamp = System.currentTimeMillis(),
                lastModified = System.currentTimeMillis()
            ),
            onDismiss = {},
            onSave = {}
        )
    }
}
