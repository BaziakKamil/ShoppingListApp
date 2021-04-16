package pl.kamilbaziak.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel

@Dao
interface ShoppingListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shoppingListModel: ShoppingListModel)

    @Update
    suspend fun update(shoppingListModel: ShoppingListModel)

    @Delete
    suspend fun delete(shoppingListModel: ShoppingListModel)

    @Query("SELECT * FROM shopping_lists WHERE (completed = 'false' OR completed = 0) ORDER BY created DESC")
    fun getShoppingListsSortedByDateCreated(): LiveData<List<ShoppingListModel>>

    @Query("SELECT * FROM shopping_lists WHERE (completed = 'true' OR completed = 1) ORDER BY created DESC")
    fun getCompletedShoppingListsSortedByDateCreated(): LiveData<List<ShoppingListModel>>
}