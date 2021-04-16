package pl.kamilbaziak.shoppinglist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "shopping_list_items")
@Parcelize
data class ShoppingListItemModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val shoppingListId: Int,
    val name: String,
    val completed: Boolean = false,
    val quantity: Int = 0
) : Parcelable