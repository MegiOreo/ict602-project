package com.example.groupprojectict602;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Item> items;

    public CustomExpandableListAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_items_detail, parent, false);
        }

        TextView textViewQuantity = convertView.findViewById(R.id.quantityTextView);
        TextView textViewExpired = convertView.findViewById(R.id.expiryDateTextView);
        TextView textViewDateAdded = convertView.findViewById(R.id.dateAddedTextView);
        TextView textViewBarcode = convertView.findViewById(R.id.barcodeTextView);

        Item currentItem = items.get(groupPosition);
        textViewQuantity.setText("Quantity: " + currentItem.getQuantity());
        textViewExpired.setText("Expired: " + currentItem.getExpiryDate());
        textViewDateAdded.setText("Date Added: " + currentItem.getDateAdded());
        textViewBarcode.setText("Barcode: " + currentItem.getBarcode());


        return convertView;
    }


//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
//        }
//
//        TextView textViewQuantity = convertView.findViewById(R.id.textViewQuantity);
//        TextView textViewExpired = convertView.findViewById(R.id.textViewExpired);
//        TextView textViewDateAdded = convertView.findViewById(R.id.textViewDateAdded);
//
//        Item currentItem = items.get(groupPosition);
//        textViewQuantity.setText("Quantity: " + currentItem.getQuantity());
//        textViewExpired.setText("Expired: " + currentItem.getExpireddate());
//        textViewDateAdded.setText("Date Added: " + currentItem.getDateadded());
//
//        return convertView;
//    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}


//package com.example.groupprojectict602;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
//
//    private Context context;
//    private List<Item> items;
//
//    public CustomExpandableListAdapter(Context context, List<Item> items) {
//        this.context = context;
//        this.items = items;
//    }
//
//    @Override
//    public int getGroupCount() {
//        return items.size();
//    }
//
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return 1;
//    }
//
//    @Override
//    public Object getGroup(int groupPosition) {
//        return items.get(groupPosition);
//    }
//
//    @Override
//    public Object getChild(int groupPosition, int childPosition) {
//        return items.get(groupPosition);
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return true;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
//        }
//
//        TextView textViewName = convertView.findViewById(R.id.textViewName);
//        textViewName.setText(items.get(groupPosition).getName());
//
//        // Set OnClickListener to change background color
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setBackgroundColor(Color.GREEN);
//            }
//        });
//
//        return convertView;
//    }
//
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_items_detail, parent, false);
//        }
//
//        TextView textViewQuantity = convertView.findViewById(R.id.quantityTextView);
//        TextView textViewExpired = convertView.findViewById(R.id.expiryDateTextView);
//        TextView textViewDateAdded = convertView.findViewById(R.id.dateAddedTextView);
//
//        Item currentItem = items.get(groupPosition);
//        textViewQuantity.setText("Quantity: " + currentItem.getQuantity());
//        textViewExpired.setText("Expired: " + currentItem.getExpiryDate());
//        textViewDateAdded.setText("Date Added: " + currentItem.getDateAdded());
//
//        // Set OnClickListener to change background color
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setBackgroundColor(Color.GREEN);
//            }
//        });
//
//        return convertView;
//    }
//
//
//
////// CustomExpandableListAdapter.java
////package com.example.groupprojectict602;
////
////import android.content.Context;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.BaseExpandableListAdapter;
////import android.widget.TextView;
////
////import java.util.List;
////
////public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
////
////    private Context context;
////    private List<Item> items;
////
////    public CustomExpandableListAdapter(Context context, List<Item> items) {
////        this.context = context;
////        this.items = items;
////    }
////
////    @Override
////    public int getGroupCount() {
////        return items.size();
////    }
////
////    @Override
////    public int getChildrenCount(int groupPosition) {
////        return 1; // Each group will have one child layout to show more details
////    }
////
////    @Override
////    public Object getGroup(int groupPosition) {
////        return items.get(groupPosition);
////    }
////
////    @Override
////    public Object getChild(int groupPosition, int childPosition) {
////        return items.get(groupPosition);
////    }
////
////    @Override
////    public long getGroupId(int groupPosition) {
////        return groupPosition;
////    }
////
////    @Override
////    public long getChildId(int groupPosition, int childPosition) {
////        return childPosition;
////    }
////
////    @Override
////    public boolean hasStableIds() {
////        return true;
////    }
////
////    @Override
////    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
////        if (convertView == null) {
////            convertView = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
////        }
////
////        TextView textViewName = convertView.findViewById(R.id.textViewName);
////        textViewName.setText(items.get(groupPosition).getName());
////
////        return convertView;
////    }
////
////    @Override
////    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
////        if (convertView == null) {
////            convertView = LayoutInflater.from(context).inflate(R.layout.list_items_detail, parent, false);
////        }
////
////        TextView textViewQuantity = convertView.findViewById(R.id.quantityTextView);
////        TextView textViewExpired = convertView.findViewById(R.id.expiryDateTextView);
////        TextView textViewDateAdded = convertView.findViewById(R.id.dateAddedTextView);
////
////        Item currentItem = items.get(groupPosition);
////        textViewQuantity.setText("Quantity: " + currentItem.getQuantity());
////        textViewExpired.setText("Expired: " + currentItem.getExpiryDate());
////        textViewDateAdded.setText("Date Added: " + currentItem.getDateAdded());
////
////        return convertView;
////    }
//
//
////    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
////        if (convertView == null) {
////            convertView = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
////        }
////
////        TextView textViewQuantity = convertView.findViewById(R.id.textViewQuantity);
////        TextView textViewExpired = convertView.findViewById(R.id.textViewExpired);
////        TextView textViewDateAdded = convertView.findViewById(R.id.textViewDateAdded);
////
////        Item currentItem = items.get(groupPosition);
////        textViewQuantity.setText("Quantity: " + currentItem.getQuantity());
////        textViewExpired.setText("Expired: " + currentItem.getExpireddate());
////        textViewDateAdded.setText("Date Added: " + currentItem.getDateadded());
////
////        return convertView;
////    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return false;
//    }
//}
