package pl.kamilbaziak.shoppinglist.ui.fragmentcompletedshoppinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import dagger.hilt.android.AndroidEntryPoint;
import pl.kamilbaziak.shoppinglist.databinding.FragmentCompletedShoppingListBinding;
import pl.kamilbaziak.shoppinglist.databinding.FragmentMainTabbedBinding;

@AndroidEntryPoint
public class CompletedShoppingListsFragment extends Fragment {

    private FragmentCompletedShoppingListBinding binding;
    private CompletedShoppingListViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CompletedShoppingListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCompletedShoppingListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CompletedShoppingListAdapter adapter = new CompletedShoppingListAdapter(requireContext());

        binding.recyclerViewCompletedShoppingList.setAdapter(adapter);
        binding.recyclerViewCompletedShoppingList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewCompletedShoppingList.setHasFixedSize(true);

        viewModel.getCompletedShoppingList().observe(getViewLifecycleOwner(), adapter::submitList);
    }
}
