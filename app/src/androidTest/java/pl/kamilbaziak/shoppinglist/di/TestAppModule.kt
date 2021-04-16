package pl.kamilbaziak.shoppinglist.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.kamilbaziak.shoppinglist.data.ShoppingListDatabase
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class TestAppModule {

    @Provides
    @Named("test_shopping_list_database")
    fun provideTestDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, ShoppingListDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}