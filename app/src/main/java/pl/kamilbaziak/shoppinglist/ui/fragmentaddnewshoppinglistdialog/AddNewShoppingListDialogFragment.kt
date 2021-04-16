package pl.kamilbaziak.shoppinglist.ui.fragmentaddnewshoppinglistdialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kamilbaziak.shoppinglist.R
import pl.kamilbaziak.shoppinglist.databinding.AddShoppingListItemFragmentBinding

@AndroidEntryPoint
class AddNewShoppingListDialogFragment: DialogFragment(R.layout.add_shopping_list_item_fragment) {

    private val viewModel: AddNewShoppingListDialogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = AddShoppingListItemFragmentBinding.bind(view)

        binding.apply {
            fabSaveTask.setOnClickListener {
                viewModel.validateAndPrepareShoppingListItemForSaving(editTextTaskName.text.toString(), editTextQuantity.text.toString())
            }


        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addNewShoppingListDialogEvent.collect { event ->
                when(event){
                    is AddNewShoppingListDialogEvent.ErrorChangingQuantityToInt -> Snackbar.make(requireView(), "Quantity is wrong, please correct it", Snackbar.LENGTH_LONG).show()

                    AddNewShoppingListDialogEvent.ItemSavedReturnToPreviousFragment -> dismiss()
                }
            }
        }
    }
}