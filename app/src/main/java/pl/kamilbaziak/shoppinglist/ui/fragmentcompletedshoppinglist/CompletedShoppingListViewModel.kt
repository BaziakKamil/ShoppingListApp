package pl.kamilbaziak.shoppinglist.ui.fragmentcompletedshoppinglist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import pl.kamilbaziak.shoppinglist.data.ShoppingListRepository
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel
import javax.inject.Inject

@HiltViewModel
class CompletedShoppingListViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _completedShoppingList = shoppingListRepository.getCompletedShoppingListsSortedByDateCreated
    val completedShoppingList = _completedShoppingList
}