package pl.kamilbaziak.shoppinglist.ui.fragmentshoppinglist

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
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
import pl.kamilbaziak.shoppinglist.databinding.FragmentShoppingListBinding
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel
import pl.kamilbaziak.shoppinglist.ui.maintabbedfragment.MainTabbedFragmentDirections

@AndroidEntryPoint
class ShoppingListFragment : Fragment(R.layout.fragment_shopping_list),
    ShoppingListAdapter.OnItemClickListener {

    private val viewModel: ShoppingListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentShoppingListBinding.bind(view)

        val shoppingListAdapter = ShoppingListAdapter(this, requireContext())

        //binding values to the layout
        binding.apply {
            recyclerViewShoppingList.apply {
                adapter = shoppingListAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
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
                    val shoppingListModel =
                        shoppingListAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onDeleteShoppingList(shoppingListModel)
                }
            }).attachToRecyclerView(recyclerViewShoppingList)

            fabAddNewShoppingList.setOnClickListener {
                buildAddNewShoppingListAlertDialog()
            }
        }

        //TODO long click for deletion

        //adding list to adapter
        viewModel.shoppingList.observe(viewLifecycleOwner){
            shoppingListAdapter.submitList(it)
        }

        //handling event from viewModel
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.shoppingListEvent.collect { event ->
                when(event)
                {
                    //showing snackbar when shopping list is deleted
                    is ShoppingListViewModel.ShoppingListEvent.ShowUndoDeleteTaskMessage ->
                    {
                        Snackbar.make(requireView(), "Shoppping list deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO"){
                                viewModel.onUndoDeleteClick(event.shoppingListModel)
                            }.show()
                    }

                    //navigating to shopping items list
                    is ShoppingListViewModel.ShoppingListEvent.NavigateToShoppingItemsList ->
                    {
                        val action = MainTabbedFragmentDirections.actionMainTabbedFragmentToFragmentShoppingItemsList2(event.shoppingListModel, event.shoppingListModel.name)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    //build dialog
    private fun buildAddNewShoppingListAlertDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.giveNewShoppingListName))
        val input = EditText(requireContext())
        input.hint = getString(R.string.enterName)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton(getString(R.string.ok_text), DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            viewModel.addNewShoppingList(input.text.toString())
            dialog.dismiss()
        })
        builder.setNegativeButton(getString(R.string.cancel_text), DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        builder.show()
    }

    override fun onItemClicked(shoppingListModel: ShoppingListModel) {
        viewModel.onNavigateToShoppingItemsList(shoppingListModel)
    }
}