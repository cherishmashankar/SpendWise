package com.codingchallenge.spendwise.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingchallenge.spendwise.R
import com.codingchallenge.spendwise.domain.model.TransactionType
import com.codingchallenge.spendwise.presentation.theme.AppColors
import com.codingchallenge.spendwise.presentation.theme.SpendWiseTheme

@Composable
fun IncomeExpenseSelector(
    type: TransactionType,
    onTypeChange: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val incomeColor = AppColors.Income
    val expenseColor = AppColors.Expense
    val unselectedColor = AppColors.unselectedColor



    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Income Button ---
        Button(
            onClick = { onTypeChange(TransactionType.INCOME) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (type == TransactionType.INCOME)
                    incomeColor else unselectedColor,
                contentColor = if (type == TransactionType.INCOME)
                    Color.White else MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.filter_income),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        // --- Expense Button ---
        Button(
            onClick = { onTypeChange(TransactionType.EXPENSE) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (type == TransactionType.EXPENSE)
                    expenseColor else unselectedColor,
                contentColor = if (type == TransactionType.EXPENSE)
                    Color.White else MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.expenses),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IncomeExpenseSelectorPreview() {
    SpendWiseTheme {
        var selectedType by remember { mutableStateOf(TransactionType.INCOME) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Select Transaction Type:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            IncomeExpenseSelector(
                type = selectedType,
                onTypeChange = { selectedType = it }
            )
        }
    }
}
