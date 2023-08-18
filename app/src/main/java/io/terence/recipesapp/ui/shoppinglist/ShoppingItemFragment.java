package io.terence.recipesapp.ui.shoppinglist;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDateTime;

import io.terence.recipesapp.R;
import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.ShoppingListDao;
import io.terence.recipesapp.entities.ShoppingItem;

/**
 * A fragment representing a list of Items.
 */
public class ShoppingItemFragment extends Fragment {
    private AppDatabase appDatabase;
    private ShoppingListDao shoppingListDao;
    private ShoppingListViewModel shoppingListViewModel;
    private MyShoppingItemRecyclerViewAdapter myShoppingItemRecyclerViewAdapter;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShoppingItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public ShoppingItemFragment newInstance(int columnCount) {
        ShoppingItemFragment fragment = new ShoppingItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shoppingitem_list, container, false);
        myShoppingItemRecyclerViewAdapter = new MyShoppingItemRecyclerViewAdapter(this::markShoppingItemBought);

        shoppingListViewModel = new ViewModelProvider(requireActivity()).get(ShoppingListViewModel.class);
        appDatabase = AppDatabase.getInstance(requireActivity().getApplicationContext());
        shoppingListDao = appDatabase.shoppingListDao();

        // Set the adapter
        if (view instanceof RecyclerView recyclerView) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            shoppingListViewModel.getShoppingList().observe(getViewLifecycleOwner(),
                    shoppingItems -> myShoppingItemRecyclerViewAdapter.submitList(shoppingItems));
            ShoppingListHeaderAdapter shoppingListHeaderAdapter = new ShoppingListHeaderAdapter();
            ConcatAdapter concatAdapter = new ConcatAdapter(shoppingListHeaderAdapter, myShoppingItemRecyclerViewAdapter);
            recyclerView.setAdapter(concatAdapter);
        }
        return view;
    }

    private void markShoppingItemBought(ShoppingItem shoppingItem){
        shoppingItem.setModifiedDate(LocalDateTime.now());
        shoppingItem.setBought(true);
        shoppingListDao.update(shoppingItem);
    }
}