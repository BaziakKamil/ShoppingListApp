package pl.kamilbaziak.shoppinglist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.kamilbaziak.shoppinglist.di.ApplicationScope
import pl.kamilbaziak.shoppinglist.model.ShoppingListItemModel
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [ShoppingListItemModel::class, ShoppingListModel::class], version = 1, exportSchema = false)
abstract class ShoppingListDatabase: RoomDatabase()
{
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun shoppingItemListDao(): ShoppingItemsListDao
    val shoppingListRepository = ShoppingListRepository(shoppingListDao())
    val shoppingItemsListRepository = ShoppingListItemListRepository(shoppingItemListDao())

    //when app launches, dummy content will be available
    class Callback @Inject constructor(
        private val database: Provider<ShoppingListDatabase>,
        @ApplicationScope private val applicaitionScope: CoroutineScope
    ) : RoomDatabase.Callback()
    {
        override fun onCreate(db: SupportSQLiteDatabase)
        {
            super.onCreate(db)
            //declaring two dao's
            val shoppingListRepository = database.get().shoppingListRepository
            val shoppingItemsListRepository = database.get().shoppingItemsListRepository

            applicaitionScope.launch{
                //adding three shopping lists with two item in each
                val list = ArrayList<ShoppingListModel>()
                list.add(ShoppingListModel(0, "First dummy content list", false))
                list.add(ShoppingListModel(1, "Second dummy content list", false))
                list.add(ShoppingListModel(2, "Third dummy content list", false))

                //adding dummy content to database
                for (shoppingList in list)
                {
                    shoppingListRepository.insert(shoppingList)
                    shoppingItemsListRepository.insert(ShoppingListItemModel(0, shoppingList.id, "First Shopping list item for " + shoppingList.name, false))
                    shoppingItemsListRepository.insert(ShoppingListItemModel(1, shoppingList.id, "Second Shopping list item for " + shoppingList.name, false))
                }
            }
        }
    }
}