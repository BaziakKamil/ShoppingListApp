package pl.kamilbaziak.shoppinglist.ui.fragmentcompletedshoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.kamilbaziak.shoppinglist.R
import pl.kamilbaziak.shoppinglist.databinding.ShoppingListRowBinding
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel

class CompletedShoppingListAdapter(private val context: Context) : ListAdapter<ShoppingListModel, CompletedShoppingListAdapter.CompletedShoppingListViewHolder>(DiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedShoppingListViewHolder
    {
        val binding = ShoppingListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompletedShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompletedShoppingListViewHolder, position: Int)
    {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    //uzywamy viewBidingu do przekazywania kodu
    inner class CompletedShoppingListViewHolder(private val bidining: ShoppingListRowBinding): RecyclerView.ViewHolder(bidining.root)
    {
        fun bind(shoppingListModel: ShoppingListModel)
        {
            bidining.apply {
                textViewName.text = shoppingListModel.name
                textViewDateCreated.text = context.getString(R.string.created_text_with_argument, shoppingListModel.createdDateFormatted) //replacing typed in string resources
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<ShoppingListModel>()
    {
        override fun areItemsTheSame(oldItem: ShoppingListModel, newItem: ShoppingListModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ShoppingListModel, newItem: ShoppingListModel): Boolean =
            oldItem == newItem
    }
}