package pl.kamilbaziak.shoppinglist.ui.fragmentaddnewshoppinglistdialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kamilbaziak.shoppinglist.R
import pl.kamilbaziak.shoppinglist.databinding.AddEditShoppingListItemFragmentBinding


@AndroidEntryPoint
class AddEditNewShoppingListDialogFragment: DialogFragment(R.layout.add_edit_shopping_list_item_fragment) {

    private val viewModel: AddNewShoppingListDialogViewModel by viewModels()

    override fun onStart() {
        super.onStart()

        //setting layout to full widht and centered
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setGravity(Gravity.CENTER)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = AddEditShoppingListItemFragmentBinding.bind(view)

        binding.apply {
            fabSaveTask.setOnClickListener {
                viewModel.validateAndPrepareShoppingListItemForSaving(
                    editTextTaskName.text.toString(),
                    editTextQuantity.text.toString()
                )
            }

            editTextTaskName.setText(viewModel.shoppingListItemName)
            editTextQuantity.setText(viewModel.shoppingListItemQuantity.toString())
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addNewShoppingListDialogEvent.collect { event ->
                when(event){
                    is AddNewShoppingListDialogEvent.ErrorChangingQuantityToInt -> Snackbar.make(
                        requireView(),
                        "Quantity is wrong, please correct it",
                        Snackbar.LENGTH_LONG
                    ).show()

                    AddNewShoppingListDialogEvent.ItemSavedReturnToPreviousFragment -> dismiss()
                }
            }
        }
    }
}