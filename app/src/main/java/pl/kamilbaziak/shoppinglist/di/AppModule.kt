package pl.kamilbaziak.shoppinglist.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import pl.kamilbaziak.shoppinglist.data.ShoppingListDatabase
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: ShoppingListDatabase.Callback
    ) =
        Room.databaseBuilder(app, ShoppingListDatabase::class.java, "shopping_list_database")
            .fallbackToDestructiveMigration() //adding dummy content to database
            .addCallback(callback)
            .build()

    @Provides
    fun provideShoppingListDao(db: ShoppingListDatabase) = db.shoppingListDao()

    @Provides
    fun provideShoppingItemsListDao(db: ShoppingListDatabase) = db.shoppingItemListDao()

    @Provides
    fun provideShoppingItemsListRepository(db: ShoppingListDatabase) = db.shoppingItemsListRepository

    @Provides
    fun provideShoppingListRepository(db: ShoppingListDatabase) = db.shoppingListRepository

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope