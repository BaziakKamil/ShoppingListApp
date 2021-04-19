package pl.kamilbaziak.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.room.Dao
import pl.kamilbaziak.shoppinglist.model.ShoppingListItemModel
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel
import javax.inject.Inject

class ShoppingListItemListRepository @Inject constructor(
    private val shoppingItemsListDao: ShoppingItemsListDao
) {
    suspend fun insert(shoppingListItemModel: ShoppingListItemModel) {
        shoppingItemsListDao.insert(shoppingListItemModel)
    }

    suspend fun update(shoppingListItemModel: ShoppingListItemModel) {
        shoppingItemsListDao.update(shoppingListItemModel)
    }

    suspend fun delete(shoppingListItemModel: ShoppingListItemModel) {
        shoppingItemsListDao.delete(shoppingListItemModel)
    }

    fun getShoppingItemListForShoppingListId(id: Int): LiveData<List<ShoppingListItemModel>> =
        shoppingItemsListDao.getShoppingItemListForShoppingListId(id).asLiveData()

    suspend fun deleteShoppingListItemForCompletedShoppingListId(id: Int) =
        shoppingItemsListDao.deleteShoppingListItemForCompletedShoppingListId(id)
}