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

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    //private List<Item> itemList;
    private OnItemClickListener listener;

    public ItemAdapter(FragmentActivity searchFragment, List<Item> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }


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

    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item);

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewCategory;
        TextView textViewDescription;
        TextView textViewBarcode;
        // Add other TextViews as needed

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewDescription = itemView.findViewById(R.id.textViewQuantity);
            textViewBarcode = itemView.findViewById(R.id.textViewBarcode);
            // Initialize other TextViews as needed
        }

        public void bind(Item item) {
            textViewName.setText("Name: " + item.getName());
            textViewCategory.setText("Category: " + item.getCategory());
            textViewDescription.setText("Quantity: " + item.getQuantity());
            textViewBarcode.setText("Barcode: " + item.getBarcode());
            // Set other TextViews with item details as needed
        }
    }

    public void searchDataList(ArrayList<Item> searchList){
        itemList = searchList;
        notifyDataSetChanged();
    }

}
