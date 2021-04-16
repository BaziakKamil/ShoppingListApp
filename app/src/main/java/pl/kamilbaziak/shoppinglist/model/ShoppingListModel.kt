package pl.kamilbaziak.shoppinglist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "shopping_lists")
@Parcelize
data class ShoppingListModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val completed: Boolean = false,
    val created: Long = System.currentTimeMillis()
): Parcelable{

    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)

}