package pl.kamilbaziak.shoppinglist.ui.fragmentshoppinglist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import pl.kamilbaziak.shoppinglist.data.ShoppingListRepository
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val shoppingListChannel = Channel<ShoppingListEvent>()
    val shoppingListEvent = shoppingListChannel.receiveAsFlow()

    //list of shopping items
    private val _shoppingList = getItems()
    val shoppingList = _shoppingList

    private fun getItems() = shoppingListRepository.getShoppingListsSortedByDateCreated

    fun onDeleteShoppingList(shoppingListModel: ShoppingListModel) = viewModelScope.launch {
        //deleting shopping list
        shoppingListRepository.delete(shoppingListModel)
        //showing snack after deletion to prevent from deleting item
        shoppingListChannel.send(ShoppingListEvent.ShowUndoDeleteTaskMessage(shoppingListModel))
    }

    fun onUndoDeleteClick(shoppingListModel: ShoppingListModel) = viewModelScope.launch {
        shoppingListRepository.insert(shoppingListModel)
    }

    fun onNavigateToShoppingItemsList(shoppingListModel: ShoppingListModel) = viewModelScope.launch {
        shoppingListChannel.send(ShoppingListEvent.NavigateToShoppingItemsList(shoppingListModel))
    }

    fun addNewShoppingList(name: String) {
        viewModelScope.launch {
            shoppingListRepository.insert(ShoppingListModel(0, name))
        }
    }

    //for "communication" with fragment purposes
    sealed class ShoppingListEvent
    {
        data class NavigateToShoppingItemsList(val shoppingListModel: ShoppingListModel) : ShoppingListEvent()
        data class ShowUndoDeleteTaskMessage(val shoppingListModel: ShoppingListModel) : ShoppingListEvent()
    }
}