package com.example.groupprojectict602;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomExpandableSearch extends BaseExpandableListAdapter {

    private Context context;
    private List<Item> items;
    private List<Item> filteredList;

    public CustomExpandableSearch(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
        this.filteredList = new ArrayList<>(items);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1; // Each group will have one child layout to show more details
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textViewName);
        textViewName.setText(items.get(groupPosition).getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_item_details, parent, false);
        }

        TextView textViewCategory = convertView.findViewById(R.id.categoryTextView);
        TextView textViewQuantity = convertView.findViewById(R.id.quantityTextView);
        TextView textViewExpired = convertView.findViewById(R.id.expiryDateTextView);
        TextView textViewDateAdded = convertView.findViewById(R.id.dateAddedTextView);
        TextView textViewBarcode = convertView.findViewById(R.id.barcodeTextView);

        Item currentItem = items.get(groupPosition);
        textViewCategory.setText("Category: " + currentItem.getCategory());
        textViewQuantity.setText("Quantity: " + currentItem.getQuantity());
        textViewExpired.setText("Expired: " + currentItem.getExpiryDate());
        textViewDateAdded.setText("Date Added: " + currentItem.getDateAdded());
        textViewBarcode.setText("Barcode: " + currentItem.getBarcode());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setFilteredList(List<Item> filteredList) {
        this.filteredList = new ArrayList<>(filteredList);
        notifyDataSetChanged(); // Notify the adapter about the data change
    }

    // Method to remove an item from the list
    public void removeItem(int position) {
        if (position < items.size()) {
            items.remove(position);
            notifyDataSetChanged(); // Notify adapter that data has changed
        }
    }
}
