package pl.kamilbaziak.shoppinglist.ui.fragmentshoppinglistitemlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.kamilbaziak.shoppinglist.R
import pl.kamilbaziak.shoppinglist.databinding.ShoppingListItemRowBinding
import pl.kamilbaziak.shoppinglist.model.ShoppingListItemModel

class ShoppingListItemListAdapter(
    private val listener: OnItemClickListener,
    private val context: Context
) : ListAdapter<ShoppingListItemModel, ShoppingListItemListAdapter.ShoppingListItemListViewHolder>(
    DiffCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingListItemListViewHolder {
        val binding =
            ShoppingListItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingListItemListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingListItemListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    //uzywamy viewBidingu do przekazywania kodu
    inner class ShoppingListItemListViewHolder(private val bidining: ShoppingListItemRowBinding) :
        RecyclerView.ViewHolder(bidining.root) {
        init {
            bidining.apply {
                checkboxCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkboxCompleted.isChecked)
                    }
                }
            }
        }

        fun bind(shoppingListItemModel: ShoppingListItemModel) {
            bidining.apply {
                textViewName.text = shoppingListItemModel.name
                //setting quantity value
                textViewQuantity.text = context.getString(
                    R.string.quantity_with_argument,
                    shoppingListItemModel.quantity.toString()
                )
                //setting checkbox value
                checkboxCompleted.isChecked = shoppingListItemModel.completed
            }
        }
    }

    interface OnItemClickListener {
        fun onCheckBoxClick(shoppingListItemModel: ShoppingListItemModel, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<ShoppingListItemModel>() {
        override fun areItemsTheSame(
            oldItem: ShoppingListItemModel,
            newItem: ShoppingListItemModel
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ShoppingListItemModel,
            newItem: ShoppingListItemModel
        ): Boolean = oldItem.name == newItem.name

    }
}