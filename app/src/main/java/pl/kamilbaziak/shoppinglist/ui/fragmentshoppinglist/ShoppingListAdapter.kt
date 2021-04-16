package pl.kamilbaziak.shoppinglist.ui.fragmentshoppinglist

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.kamilbaziak.shoppinglist.R
import pl.kamilbaziak.shoppinglist.databinding.ShoppingListRowBinding
import pl.kamilbaziak.shoppinglist.model.ShoppingListModel

class ShoppingListAdapter(private val listener: OnItemClickListener, private val context: Context) : ListAdapter<ShoppingListModel, ShoppingListAdapter.ShoppingListViewHolder>(DiffCallback())
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder
    {
        val binding = ShoppingListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int)
    {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    //uzywamy viewBidingu do przekazywania kodu
    inner class ShoppingListViewHolder(private val bidining: ShoppingListRowBinding): RecyclerView.ViewHolder(bidining.root)
    {
        init {
            bidining.apply {
                root.setOnClickListener{
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION)
                    {
                        val shoppingList = getItem(position)
                        listener.onItemClicked(shoppingList)
                    }
                }
            }
        }

        fun bind(shoppingListModel: ShoppingListModel)
        {
            bidining.apply {
                textViewName.text = shoppingListModel.name
                textViewDateCreated.text = context.getString(R.string.created_text_with_argument, shoppingListModel.createdDateFormatted) //replacing typed in string resources
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClicked(shoppingListModel: ShoppingListModel)
    }

    class DiffCallback: DiffUtil.ItemCallback<ShoppingListModel>()
    {
        override fun areItemsTheSame(oldItem: ShoppingListModel, newItem: ShoppingListModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ShoppingListModel, newItem: ShoppingListModel): Boolean =
            oldItem == newItem
    }
}