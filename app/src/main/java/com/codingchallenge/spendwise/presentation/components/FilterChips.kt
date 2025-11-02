package com.codingchallenge.spendwise.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codingchallenge.spendwise.R
import com.codingchallenge.spendwise.presentation.spendwise.TransactionFilter
import com.codingchallenge.spendwise.presentation.theme.AppDimens

@Composable
fun FilterChips(
    selectedFilter: TransactionFilter,
    onFilterSelected: (TransactionFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        TransactionFilter.All,
        TransactionFilter.Income,
        TransactionFilter.Expense
    )

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppDimens.PaddingSmall)
    ) {
        items(filters) { filter ->
            val isSelected = selectedFilter == filter
            val label = when (filter) {
                TransactionFilter.All -> stringResource(R.string.filter_all)
                TransactionFilter.Income -> stringResource(R.string.filter_income)
                TransactionFilter.Expense -> stringResource(R.string.filter_expenses)
            }

            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = label,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = when (filter) {
                        TransactionFilter.Income -> MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                        TransactionFilter.Expense -> MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                        else -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
                    },
                    containerColor = Color.Transparent
                ),
                border = if (isSelected) null else BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.9f)
                )
            )
        }
    }
}


