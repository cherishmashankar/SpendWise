package com.codingchallenge.spendwise.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.domain.model.TransactionType
import com.codingchallenge.spendwise.presentation.theme.AppColors
import com.codingchallenge.spendwise.presentation.theme.AppDimens
import com.codingchallenge.spendwise.presentation.theme.SpendWiseTheme
import com.codingchallenge.spendwise.utils.toEuroFormat
import com.codingchallenge.spendwise.utils.toFormattedDate
import java.util.*

@Composable
fun TransactionItem(
    transaction: Transaction,
    locale: Locale,
    modifier: Modifier = Modifier,
    onEditClick: (Transaction) -> Unit = {},
    onDeleteClick: (Transaction) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    val icon = if (transaction.type == TransactionType.INCOME) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward
    val color = if (transaction.type == TransactionType.INCOME) AppColors.Income else AppColors.Expense

    val formattedAmount = remember(transaction.amount, locale) {
        transaction.amount.toEuroFormat(locale)
    }
    val formattedDate = remember(transaction.lastModified, locale) { transaction.lastModified.toFormattedDate(locale) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.PaddingLarge, vertical = AppDimens.PaddingXSmall)
            .clickable { showMenu = true },
        shape = RoundedCornerShape(AppDimens.CornerMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimens.PaddingXSmall),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppDimens.PaddingLarge, vertical = AppDimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = AppDimens.PaddingMedium)
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = transaction.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = if (transaction.type == TransactionType.EXPENSE)
                            "-$formattedAmount"
                        else
                            "+$formattedAmount",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = color
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))

                if (!transaction.notes.isNullOrBlank()) {
                    Text(
                        text = transaction.notes,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    if (showMenu) {
            TransactionItemMoreOptions(
                onDismiss = { showMenu = false },
                onEdit = { onEditClick(transaction) },
                onDelete = { onDeleteClick(transaction) }
            )
    }
}



@Preview(showBackground = true)
@Composable
private fun TransactionItemPreview() {
    SpendWiseTheme {
        Column(Modifier.fillMaxWidth()) {
            TransactionItem(
                transaction = Transaction(
                    id = 1,
                    title = "Salary Credit",
                    amount = 1200.00,
                    type = TransactionType.INCOME,
                    notes = "Monthly payment",
                    category = "Income",
                    timestamp = System.currentTimeMillis(),
                    lastModified = System.currentTimeMillis()
                ),
                locale = Locale.ENGLISH
            )
            TransactionItem(
                transaction = Transaction(
                    id = 2,
                    title = "Grocery Shopping",
                    amount = 75.50,
                    type = TransactionType.EXPENSE,
                    notes = "Weekly groceries",
                    category = "Food",
                    timestamp = System.currentTimeMillis(),
                    lastModified = System.currentTimeMillis()
                ),
                locale = Locale.GERMAN
            )
        }
    }
}
