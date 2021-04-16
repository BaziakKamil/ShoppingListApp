package pl.kamilbaziak.shoppinglist.ui.fragmentshoppinglistitemlist

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import pl.kamilbaziak.shoppinglist.R
import pl.kamilbaziak.shoppinglist.databinding.FragmentShoppingListItemListBinding
import pl.kamilbaziak.shoppinglist.model.ShoppingListItemModel

@AndroidEntryPoint
class ShoppingListItemListFragment : Fragment(R.layout.fragment_shopping_list_item_list),
    ShoppingListItemListAdapter.OnItemClickListener {

    private val viewModel: ShoppingListItemListViewModel by viewModels()
    private lateinit var shoppingListItemListAdapter: ShoppingListItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentShoppingListItemListBinding.bind(view)

        shoppingListItemListAdapter = ShoppingListItemListAdapter(this, requireContext())

        binding.apply {
            recyclerViewShoppingItemsList.apply {
                adapter = shoppingListItemListAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            fabAddNewShoppingItem.setOnClickListener {
                buildAddNewShoppingListItemAlertDialog()
            }

            //swipe for deletion
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                //take care of moving up and down
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                //on swipe
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val shoppingListItemListModel =
                        shoppingListItemListAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onDeleteShoppingList(shoppingListItemListModel)
                }
            }).attachToRecyclerView(recyclerViewShoppingItemsList)
        }

        //setting list to adapter
        viewModel.shoppingListItems?.observe(viewLifecycleOwner) {
            shoppingListItemListAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.shoppingListItemListEvent.collect { event ->
                when (event) {
                    is ShoppingListItemListViewModel.ShoppingListItemListEvent.ShowChangeMessage -> {
                        Snackbar.make(requireView(), "Shopping item comleted", Snackbar.LENGTH_LONG)
                            .show()
                    }

                    is ShoppingListItemListViewModel.ShoppingListItemListEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(
                            requireView(),
                            "Shoppping list item deleted",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.shoppingListItemModel)
                            }.show()
                    }

                    ShoppingListItemListViewModel.ShoppingListItemListEvent.ShoppingListIsCompletedCloseDialog -> {
                        val action =
                            ShoppingListItemListFragmentDirections.actionFragmentShoppingItemsListToMainTabbedFragment()
                        findNavController().navigate(action)
                    }

                    is ShoppingListItemListViewModel.ShoppingListItemListEvent.ErrorChangingQuantityToInt -> {
                        Snackbar.make(
                            requireView(),
                            "Quantity is wrong, please correct it",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    //build dialog
    private fun buildAddNewShoppingListItemAlertDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.giveNewShoppingListName))

        val layout = LinearLayout(requireContext())
        val quantityLayout = LinearLayout(requireContext())

        layout.orientation = LinearLayout.VERTICAL
        quantityLayout.orientation = LinearLayout.HORIZONTAL

        val nameEditText = EditText(requireContext())
        nameEditText.hint = getString(R.string.enterItemName)

        val quantityTextView = TextView(requireContext())
        quantityTextView.text = getString(R.string.quantity)

        val quantityEditText = EditText(requireContext())
        quantityEditText.hint = getString(R.string.enterQuantity)
        quantityEditText.inputType = InputType.TYPE_CLASS_NUMBER
        //TODO add padding and margin to views

        quantityLayout.addView(quantityTextView)
        quantityLayout.addView(quantityEditText)

        layout.addView(nameEditText)
        layout.addView(quantityLayout)

        builder.setView(layout)
        builder.setPositiveButton(
            getString(R.string.ok_text),
            DialogInterface.OnClickListener { dialog, which ->
                viewModel.addNewItem(nameEditText.text.toString(), quantityEditText.text.toString())
                dialog.dismiss()
            })
        builder.setNegativeButton(
            getString(R.string.cancel_text),
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.done_button_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.done_menu_button -> {
                buildCompleteListAlertDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCheckBoxClick(shoppingListItemModel: ShoppingListItemModel, isChecked: Boolean) {
        viewModel.onShoppingItemCheckedChanged(shoppingListItemModel, isChecked)
    }

    private fun buildCompleteListAlertDialog(){
        val title = getString(R.string.completeDialogTitle)
        val message = getString(R.string.completeDialogMessage1) + "\n" + getString(R.string.completeDialogMessage2)
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
       builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.yes ) { dialog, which ->
                viewModel.setShoppingListAsCompleted()
            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.no, null)
            .show()
    }
}