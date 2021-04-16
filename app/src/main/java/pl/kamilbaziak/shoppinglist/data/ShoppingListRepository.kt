package pl.kamilbaziak.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.room.Dao
import pl.kamilbaziak.shoppinglist.model.ShoppingListItemModel
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val shoppingListDao: ShoppingListDao
){
    suspend fun insert(shoppingListModel: ShoppingListModel) {
        shoppingListDao.insert(shoppingListModel)
    }

    suspend fun update(shoppingListModel: ShoppingListModel) {
        shoppingListDao.update(shoppingListModel)
    }

    suspend fun delete(shoppingListModel: ShoppingListModel) {
        shoppingListDao.delete(shoppingListModel)
    }

    val getShoppingListsSortedByDateCreated: LiveData<List<ShoppingListModel>> =
        shoppingListDao.getShoppingListsSortedByDateCreated()

    val getCompletedShoppingListsSortedByDateCreated: LiveData<List<ShoppingListModel>> =
        shoppingListDao.getCompletedShoppingListsSortedByDateCreated()
}