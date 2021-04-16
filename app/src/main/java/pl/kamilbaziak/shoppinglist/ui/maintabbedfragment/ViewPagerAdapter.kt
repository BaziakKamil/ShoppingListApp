package pl.kamilbaziak.shoppinglist.ui.maintabbedfragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import pl.kamilbaziak.shoppinglist.ui.fragmentcompletedshoppinglist.CompletedShoppingListsFragment
import pl.kamilbaziak.shoppinglist.ui.fragmentshoppinglist.ShoppingListFragment

/*
* Fixed size tabLayout adapter, should be universal in future version
* */
class ViewPagerAdapter(supportFragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(supportFragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            1 -> return CompletedShoppingListsFragment()
        }

        return ShoppingListFragment()
    }
}