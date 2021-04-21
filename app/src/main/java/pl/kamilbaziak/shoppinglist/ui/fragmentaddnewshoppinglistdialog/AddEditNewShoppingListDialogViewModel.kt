package pl.kamilbaziak.shoppinglist.ui.fragmentaddnewshoppinglistdialog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.kamilbaziak.shoppinglist.data.ShoppingListItemListRepository
import pl.kamilbaziak.shoppinglist.model.ShoppingListItemModel
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddNewShoppingListDialogViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val shoppingListItemListRepository: ShoppingListItemListRepository
) : ViewModel() {
    private val addNewShoppingListDialogChannel = Channel<AddNewShoppingListDialogEvent>()
    val addNewShoppingListDialogEvent = addNewShoppingListDialogChannel.receiveAsFlow()

    //retreving shopping list object from savedState never empty
    private val shoppingList = state.get<ShoppingListModel>("shoppingListModel")!!
    //retreving shopping list item object from savedState to edit it
    private val shoppingListItem = state.get<ShoppingListItemModel>("shoppingListItemModel")

    //getting valuet to edit
    val shoppingListItemName = state.get<String>("shoppingListItemName") ?: shoppingListItem?.name ?: ""
    val shoppingListItemQuantity = state.get<Int>("shoppingListItemQuantity") ?: shoppingListItem?.quantity ?: 0

    //transforming string value to integer and passing it to specifi function depending if this is edit or add function
    fun validateAndPrepareShoppingListItemForSaving(name: String, quantity: String) {
        var innerName = name
        var innerQuantity = quantity
        val quantityInt: Int

        if (innerName.isEmpty())
            innerName = "Brak nazwy"

        if (innerQuantity.isEmpty())
            innerQuantity = "0"

        //converting string to int
        try {
            quantityInt = innerQuantity.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            sendErrorChangingQuantity(quantity)
            return
        }

        //inserting or updateding depends on which funcon was chosen
        if(shoppingListItem != null) {
            //setting the same id from clicked item
            val item = ShoppingListItemModel(shoppingListItem.id, shoppingList.id, innerName, false, quantityInt)
            updateShoppingListItemInDatabase(item)
        }
        else
            saveNewShoppingListItemToDatabase(innerName, quantityInt)
    }

    //sending error to fragment by channel for quantity format error
    private fun sendErrorChangingQuantity(quantity: String) =
        viewModelScope.launch {
            addNewShoppingListDialogChannel.send(
                AddNewShoppingListDialogEvent.ErrorChangingQuantityToInt(
                    quantity
                )
            )
        }

    private fun saveNewShoppingListItemToDatabase(innerName: String, quantityInt: Int) =
        viewModelScope.launch {
            val item = ShoppingListItemModel(0, shoppingList.id, innerName, false, quantityInt)
            shoppingListItemListRepository.insert(item)
            addNewShoppingListDialogChannel.send(AddNewShoppingListDialogEvent.ItemSavedReturnToPreviousFragment)
        }

    private fun updateShoppingListItemInDatabase(item: ShoppingListItemModel) =
        viewModelScope.launch {
            shoppingListItemListRepository.update(item)
            addNewShoppingListDialogChannel.send(AddNewShoppingListDialogEvent.ItemSavedReturnToPreviousFragment)
        }
}

//for "communication" with fragment purposes
sealed class AddNewShoppingListDialogEvent {
    object ItemSavedReturnToPreviousFragment : AddNewShoppingListDialogEvent()
    data class ErrorChangingQuantityToInt(val text: String) : AddNewShoppingListDialogEvent()
}
