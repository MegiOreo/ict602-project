package com.example.groupprojectict602;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    private Spinner spinnerCategory;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        expandableListView = view.findViewById(R.id.expandableListView);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("items");

        // Initialize views
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.categories_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Listen to Spinner selection changes
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                fetchItemsForCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        return view;
    }

    private ExpandableListView expandableListView;

    // Inside fetchItemsForCategory method in InventoryFragment
    // Inside your InventoryFragment class
    private void fetchItemsForCategory(String category) {
        databaseReference.orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null) {
                        items.add(item);
                    }
                }

                // Create an instance of the custom ExpandableListAdapter
                CustomExpandableListAdapter customExpandableListAdapter = new CustomExpandableListAdapter(getContext(), items);

                // Set the adapter to the ExpandableListView
                expandableListView.setAdapter(customExpandableListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }



//    private void fetchItemsForCategory(String category) {
//        databaseReference.orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Item> items = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Item item = snapshot.getValue(Item.class); // Use your updated Item class constructor here
//                    if (item != null) {
//                        items.add(item);
//                        // Log the item details for debugging
//                        Log.d("ItemDetails", "Name: " + item.getName() + ", Category: " + item.getCategory());
//                    }
//                }
//
//                ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(items);
//                expandableListView.setAdapter(expandableListAdapter);
//                // TODO: Display the items in your expandable ListView
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors here
//            }
//        });
//    }
}