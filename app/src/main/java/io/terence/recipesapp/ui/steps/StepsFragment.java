package io.terence.recipesapp.ui.steps;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;

import io.terence.recipesapp.R;
import io.terence.recipesapp.config.AppDatabase;
import io.terence.recipesapp.daos.StepDao;
import io.terence.recipesapp.entities.Step;
import io.terence.recipesapp.ui.edit.NewRecipeViewModel;

/**
 * A fragment representing a list of Items.
 */
public class StepsFragment extends Fragment {

    ListAdapter<Step, StepsRecyclerViewAdapter.ViewHolder> stepsRecyclerViewAdapter;
    private AppDatabase appDatabase;
    private StepDao stepDao;
    NewRecipeViewModel newRecipeViewModel;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StepsFragment newInstance(int columnCount) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newRecipeViewModel = new ViewModelProvider(requireActivity()).get(NewRecipeViewModel.class);
        appDatabase = AppDatabase.getInstance(requireActivity().getApplicationContext());
        stepDao = appDatabase.stepDao();
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
            stepsRecyclerViewAdapter =
                    new StepsRecyclerViewAdapter(this::deleteStep, this::editStep);

            newRecipeViewModel.getRecipeWithIngredientsAndSteps().observe(getViewLifecycleOwner(), recipe -> stepsRecyclerViewAdapter.submitList(recipe.steps));
            recyclerView.setAdapter(stepsRecyclerViewAdapter);
        }
        return view;
    }
    private void deleteStep(Step step){
        stepDao.delete(step);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void editStep(Step step){
        Context context = requireActivity();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText stepDescription = new EditText(context);
        stepDescription.setHint("Step Description");
        layout.addView(stepDescription); // Notice this is an add method

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(layout)
                .setPositiveButton("Save Step", null)
                .setNegativeButton("Cancel", (dialog1, which) -> {dialog1.dismiss();})
                .create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view1 -> {
                if(stepDescription.getText().length() == 0){
                    stepDescription.setError("Step Description Must Have a Value");
                }
                if(stepDescription.getError() == null){
                    step.setDescription(stepDescription.getText().toString());
                    step.setOrder(stepDao.getStepsCount(newRecipeViewModel.getRecipeId()) + 1);
                    step.setRecipeId(newRecipeViewModel.getRecipeId());
                    step.setModifiedDate(LocalDateTime.now());
                    stepDao.upsert(step);
                    dialog.dismiss();

                    stepsRecyclerViewAdapter.notifyDataSetChanged();
                }
            });
        });
        dialog.show();
    }
}