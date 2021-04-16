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

    //retreving shopping list object from savedState
    private val shoppingList = state.get<ShoppingListModel>("shoppingListModel")!!

    fun validateAndPrepareShoppingListItemForSaving(name: String, quantity: String) {
        var innerName = name
        var innerQuantity = quantity
        var quantityInt: Int

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

        saveNewShoppingListItemToDatabase(innerName, quantityInt)
    }

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
}

//for "communication" with fragment purposes
sealed class AddNewShoppingListDialogEvent {
    object ItemSavedReturnToPreviousFragment : AddNewShoppingListDialogEvent()
    data class ErrorChangingQuantityToInt(val text: String) : AddNewShoppingListDialogEvent()
}
