package com.codingchallenge.spendwise.presentation.spendwise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingchallenge.spendwise.R
import com.codingchallenge.spendwise.domain.model.Transaction
import com.codingchallenge.spendwise.presentation.components.*
import com.codingchallenge.spendwise.presentation.theme.AppDimens
import com.codingchallenge.spendwise.utils.AppConstants
import com.codingchallenge.spendwise.utils.updateLocale
import java.util.*


@Composable
fun TransactionScreen(
    viewModel: SpendWiseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTransaction by remember { mutableStateOf<Transaction?>(null) }

    val context = LocalContext.current
    val localizedContext = remember(uiState.language) {
        context.updateLocale(uiState.language)
    }
    val locale = if (uiState.language.lowercase() == "de") Locale.GERMAN else Locale.ENGLISH

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = localizedContext.getString(R.string.add_transaction),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Spacer(modifier = Modifier.height(AppDimens.PaddingXLarge))
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::updateSearchQuery,
                currentLanguage = if (uiState.language == "de") AppLanguage.GERMAN else AppLanguage.ENGLISH,
                currentTheme = when (uiState.themeMode) {
                    AppConstants.THEME_LIGHT -> AppTheme.LIGHT
                    AppConstants.THEME_DARK -> AppTheme.DARK
                    else -> AppTheme.DARK
                },
                showChart = uiState.showChart,
                onLanguageChange = { viewModel.setLanguage(if (it == AppLanguage.GERMAN)  AppConstants.LANGUAGE_GERMAN else  AppConstants.LANGUAGE_ENGLISH) },
                onThemeChange = { viewModel.setTheme(it.name.lowercase()) },
                onToggleChart = viewModel::toggleChartVisibility,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppDimens.PaddingSmall)
            )

            FilterChips(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::updateSelectedFilter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppDimens.PaddingLarge, vertical = AppDimens.PaddingSmall)
            )


            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(AppDimens.PaddingSmall)
            ) {
                // Balance Overview (if enabled)
                if (uiState.showChart) {
                    item {
                        SectionTitle(text = stringResource(id = R.string.balance_overview_title))
                        BalanceOverviewCard(
                            summary = uiState.balanceSummary,
                            modifier = Modifier.fillMaxWidth(),
                            locale
                        )
                    }
                }

                // Transactions Section
                item {
                    SectionTitle(text = stringResource(id = R.string.transactions_list_title))
                }

                items(uiState.filteredTransactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        locale = locale,
                        onEditClick = {
                            editingTransaction = it
                            showAddDialog = true
                        },
                        onDeleteClick = { viewModel.deleteTransaction(it.id) }
                    )
                }

                item { Spacer(modifier = Modifier.height(64.dp)) }
            }
        }
    }

    // --- Add/Edit Dialog ---
    if (showAddDialog) {
        AddEditTransactionDialog(
            transaction = editingTransaction,
            onDismiss = {
                showAddDialog = false
                editingTransaction = null
            },
            onSave = { transaction ->
                viewModel.addOrUpdateTransaction(transaction)
                showAddDialog = false
                editingTransaction = null
            }
        )
    }
}

