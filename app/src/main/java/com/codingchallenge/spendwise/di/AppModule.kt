package com.codingchallenge.spendwise.di


import android.content.Context
import androidx.room.Room
import com.codingchallenge.spendwise.data.local.db.TransactionDao
import com.codingchallenge.spendwise.data.local.db.TransactionDatabase
import com.codingchallenge.spendwise.data.local.preferences.AppPreferences
import com.codingchallenge.spendwise.data.repository.PreferencesRepositoryImpl
import com.codingchallenge.spendwise.data.repository.TransactionRepositoryImpl
import com.codingchallenge.spendwise.domain.repository.PreferencesRepository
import com.codingchallenge.spendwise.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTransactionDatabase(appContext: Context): TransactionDatabase =
        Room.databaseBuilder(
            appContext,
            TransactionDatabase::class.java,
            "transaction_db"
        ).build()

    @Provides
    fun provideTransactionDao(db: TransactionDatabase): TransactionDao = db.transactionDao()

    @Provides
    @Singleton
    fun provideAppPreferences(appContext: Context): AppPreferences = AppPreferences(appContext)

    @Provides
    @Singleton
    fun provideTransactionRepository(
        dao: TransactionDao
    ): TransactionRepository = TransactionRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providePreferencesRepository(
        appPreferences: AppPreferences
    ): PreferencesRepository = PreferencesRepositoryImpl(appPreferences)

}
