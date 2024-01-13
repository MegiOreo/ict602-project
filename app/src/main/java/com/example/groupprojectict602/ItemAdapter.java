package com.example.groupprojectict602;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;

    public ItemAdapter(FragmentActivity searchFragment, List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewCategory;
        // Add other TextViews as needed

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            // Initialize other TextViews as needed
        }

        public void bind(Item item) {
            textViewName.setText(item.getName());
            textViewCategory.setText(item.getCategory());
            // Set other TextViews with item details as needed
        }
    }

    public void searchDataList(ArrayList<Item> searchList){
        itemList = searchList;
        notifyDataSetChanged();
    }

}
