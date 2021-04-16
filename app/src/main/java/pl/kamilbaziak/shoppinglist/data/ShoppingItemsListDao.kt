package pl.kamilbaziak.shoppinglist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.kamilbaziak.shoppinglist.model.ShoppingListItemModel
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel

@Dao
interface ShoppingItemsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shoppingListItemModel: ShoppingListItemModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(shoppingListItemModel: ShoppingListItemModel)

    @Delete
    suspend fun delete(shoppingListItemModel: ShoppingListItemModel)

    @Query("SELECT * FROM shopping_list_items WHERE shoppingListId = :chosenShoppingListId ORDER BY id DESC")
    fun getShoppingItemListForShoppingListId(chosenShoppingListId: Int): Flow<List<ShoppingListItemModel>>
}