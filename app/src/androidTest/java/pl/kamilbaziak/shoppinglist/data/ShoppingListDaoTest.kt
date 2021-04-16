package pl.kamilbaziak.shoppinglist.data

import android.util.Log
import androidx.lifecycle.asLiveData
import androidx.room.Delete
import androidx.test.filters.SmallTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class ShoppingListDaoTest {

    private val TAG = "ShoppingListDaoTest"

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_shopping_list_database")
    lateinit var database: ShoppingListDatabase
    private lateinit var dao: ShoppingListDao

    @Before
    fun setup(){
        hiltRule.inject()
        dao = database.shoppingListDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insert(): Unit = runBlocking {
        val shoppingListModel = ShoppingListModel(0, "Name", false)
        dao.insert(shoppingListModel)
        val shoppingList = dao.getShoppingListsSortedByDateCreated().value
        if (shoppingList != null) {
            assert(shoppingList.contains(shoppingListModel))

        }
        else
            Log.e(TAG,"Cannot find that one")
    }

    @Test
    fun delete(): Unit = runBlocking {
        val shoppingListModel = ShoppingListModel(0, "Name", false)
        dao.insert(shoppingListModel)
        dao.delete(shoppingListModel)
        val shoppingList = dao.getShoppingListsSortedByDateCreated().value
        if (shoppingList != null) {
            assert(!shoppingList.contains(shoppingListModel))
        }
        else
            Log.e(TAG, "This item exists")
    }
}