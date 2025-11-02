package com.codingchallenge.spendwise.presentation.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingchallenge.spendwise.R
import com.codingchallenge.spendwise.domain.model.BalanceSummary
import com.codingchallenge.spendwise.presentation.theme.AppColors
import com.codingchallenge.spendwise.presentation.theme.AppDimens
import com.codingchallenge.spendwise.utils.toEuroFormat
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import java.util.*

@Composable
fun BalanceOverviewCard(
    summary: BalanceSummary,
    modifier: Modifier = Modifier,
    locale: Locale,
) {
    val incomeColor = AppColors.Income
    val expenseColor = AppColors.Expense


    val chartData = PieChartData(
        slices = listOf(
            PieChartData.Slice(summary.totalIncome.toFloat(), incomeColor),
            PieChartData.Slice(summary.totalExpense.toFloat(), expenseColor)
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.PaddingLarge, vertical = AppDimens.PaddingXSmall),
        shape = RoundedCornerShape(AppDimens.CornerMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimens.PaddingXSmall),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.PaddingLarge),
        ) {
            // --- Title ---
            Text(
                text = stringResource(R.string.balance),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(AppDimens.PaddingSmall))

            // --- Total Balance ---
            Text(
                text = summary.totalBalance.toEuroFormat(locale),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(AppDimens.PaddingMedium))

            // --- Income & Expense with Chart ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(R.string.income),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(AppDimens.PaddingSmall))
                    Text(
                        text = summary.totalIncome.toEuroFormat(locale),
                        color = incomeColor,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(AppDimens.PaddingMedium))

                    Text(
                        text = stringResource(R.string.expenses),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    )
                    Spacer(modifier = Modifier.height(AppDimens.PaddingSmall))
                    Text(
                        text = summary.totalExpense.toEuroFormat(locale),
                        color = expenseColor,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                PieChart(
                    pieChartData = chartData,
                    sliceDrawer = SimpleSliceDrawer(sliceThickness = 60f),
                    modifier = Modifier.size(100.dp)

                )

            }

        }
    }
}


