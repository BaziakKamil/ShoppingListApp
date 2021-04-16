package pl.kamilbaziak.shoppinglist.ui.fragmentshoppinglistitemlist

import androidx.lifecycle.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.kamilbaziak.shoppinglist.data.ShoppingItemsListDao
import pl.kamilbaziak.shoppinglist.data.ShoppingListItemListRepository
import pl.kamilbaziak.shoppinglist.data.ShoppingListRepository
import pl.kamilbaziak.shoppinglist.model.ShoppingListItemModel
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel
import pl.kamilbaziak.shoppinglist.ui.fragmentaddnewshoppinglistdialog.AddNewShoppingListDialogViewModel
import pl.kamilbaziak.shoppinglist.ui.fragmentshoppinglist.ShoppingListViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ShoppingListItemListViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val shoppingListItemListRepository: ShoppingListItemListRepository,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    private val shoppingListItemListChannel =
        Channel<ShoppingListItemListViewModel.ShoppingListItemListEvent>()
    val shoppingListItemListEvent = shoppingListItemListChannel.receiveAsFlow()

    //retreving shopping list object from savedState
    private val shoppingList = state.get<ShoppingListModel>("shoppingListModel")!!

    private var _shoppingListItems =
        shoppingListItemListRepository.getShoppingItemListForShoppingListId(shoppingList.id)

    val shoppingListItems = _shoppingListItems

    fun onShoppingItemCheckedChanged(
        shoppingListItemModel: ShoppingListItemModel,
        isChecked: Boolean
    ) = viewModelScope.launch {
        shoppingListItemListRepository.update(shoppingListItemModel.copy(completed = isChecked))
    }

    fun setShoppingListAsCompleted(){
        viewModelScope.launch {
            shoppingListRepository.update(shoppingList.copy(completed = true))
            shoppingListItemListChannel.send(ShoppingListItemListEvent.ShoppingListIsCompletedCloseDialog)
        }
    }

    fun onDeleteShoppingList(shoppingListItemModel: ShoppingListItemModel) = viewModelScope.launch {
        //deleting shopping list
        shoppingListItemListRepository.delete(shoppingListItemModel)
        //showing snack after deletion to prevent from deleting item
        shoppingListItemListChannel.send(
            ShoppingListItemListEvent.ShowUndoDeleteTaskMessage(
                shoppingListItemModel
            )
        )
    }

    fun onUndoDeleteClick(shoppingListItemModel: ShoppingListItemModel) = viewModelScope.launch {
        shoppingListItemListRepository.insert(shoppingListItemModel)
    }

    fun addNewItem(name: String, quantity: String) {
        viewModelScope.launch {
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
                shoppingListItemListChannel.send(ShoppingListItemListEvent.ErrorChangingQuantityToInt(quantity))
                return@launch
            }
            shoppingListItemListRepository.insert(ShoppingListItemModel(0, shoppingList.id, name, false, quantityInt))
        }
    }

    //for "communication" with fragment purposes
    sealed class ShoppingListItemListEvent {
        data class ShowChangeMessage(val shoppingListItemModel: ShoppingListItemModel) :
            ShoppingListItemListEvent()

        data class ShowUndoDeleteTaskMessage(val shoppingListItemModel: ShoppingListItemModel) :
            ShoppingListItemListEvent()

        object ShoppingListIsCompletedCloseDialog : ShoppingListItemListEvent()

        data class ErrorChangingQuantityToInt(val text: String) : ShoppingListItemListEvent()
    }
}