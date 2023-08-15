package io.terence.recipesapp.ui.steps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import io.terence.recipesapp.R;
import io.terence.recipesapp.entities.Step;
import io.terence.recipesapp.ui.reflow.NewRecipeViewModel;

/**
 * A fragment representing a list of Items.
 */
public class ItemStepsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemStepsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemStepsFragment newInstance(int columnCount) {
        ItemStepsFragment fragment = new ItemStepsFragment();
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
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);

        NewRecipeViewModel newRecipeViewModel =
                new ViewModelProvider(requireActivity()).get(NewRecipeViewModel.class);
        // Set the adapter
        if (view instanceof RecyclerView recyclerView) {
            Context context = view.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            ListAdapter<Step, StepsRecyclerViewAdapter.ViewHolder> stepsRecyclerViewAdapter = new StepsRecyclerViewAdapter();

            newRecipeViewModel.getRecipeWithIngredientsAndSteps().observe(getViewLifecycleOwner(), recipe -> stepsRecyclerViewAdapter.submitList(recipe.steps));
            recyclerView.setAdapter(stepsRecyclerViewAdapter);
        }
        return view;
    }

}