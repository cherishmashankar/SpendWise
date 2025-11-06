package com.codingchallenge.spendwise.presentation.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingchallenge.spendwise.R
import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.domain.model.TransactionType
import com.codingchallenge.spendwise.presentation.theme.AppDimens
import java.text.NumberFormat
import java.util.Locale

@SuppressLint("UnrememberedMutableState")
@Composable
fun AddEditTransactionDialog(
    transaction: Transaction? = null,
    onDismiss: () -> Unit,
    onSave: (Transaction) -> Unit,
    locale: Locale
) {
    var title by remember { mutableStateOf(transaction?.title ?: "") }
    var amount by remember {
        mutableStateOf(
            transaction?.amount?.let {
                NumberFormat.getNumberInstance(locale).format(it)
            } ?: ""
        )
    }
    var note by remember { mutableStateOf(transaction?.notes ?: "") }
    var type by remember { mutableStateOf(transaction?.type ?: TransactionType.INCOME) }

    var titleError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val isEditing = transaction != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingSmall)
            ) {

                BasicTextField(
                    value = title,
                    onValueChange = {
                        title = it.replace("\n", "")
                        titleError = false
                    },
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

                if (titleError) {
                    Text(
                        text = stringResource(R.string.error_empty_title),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(AppDimens.PaddingSmall))

                // ---- Type Selector ----
                IncomeExpenseSelector(type = type, onTypeChange = { type = it })

                // ---- Amount Field ----
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AmountInputField(
                        amount = amount,
                        onAmountChange = {
                            amount = it
                            amountError = false
                        },
                        type = type,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (amountError) {
                    Text(
                        text = stringResource(R.string.error_invalid_amount),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }

                // ---- Notes ----
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
                    val cleanAmount = amount.replace(",", ".")
                    val parsedAmount = cleanAmount.toDoubleOrNull() ?: 0.0
                    var hasError = false

                    if (title.trim().isEmpty()) {
                        titleError = true
                        hasError = true
                    }
                    if (parsedAmount <= 0) {
                        amountError = true
                        hasError = true
                    }

                    if (!hasError) {
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
                    }
                },
                enabled = true
            ) {
                Text(
                    text = if (isEditing) stringResource(R.string.save) else stringResource(R.string.add),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    )
}

