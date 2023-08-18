package io.terence.recipesapp.ui.shoppinglist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.terence.recipesapp.R;

public class ShoppingListHeaderAdapter extends RecyclerView.Adapter<ShoppingListHeaderAdapter.ViewHolder>{

    @NonNull
    @Override
    public ShoppingListHeaderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.shopping_item_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mShoppingItemName = itemView.findViewById(R.id.shopping_item_header);
        public final TextView mShoppingItemAddedDate = itemView.findViewById(R.id.shopping_item_added_date_header);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mShoppingItemName.setText("Item Name");
            mShoppingItemAddedDate.setText("Date Added");
        }
    }
}
