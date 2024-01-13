package com.example.groupprojectict602;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

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
    private ExpandableListView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        expandableListView = view.findViewById(R.id.expandableListView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("items");
        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        fetchCategoriesAndPopulateSpinner();

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
        //start edit

        // Set OnItemLongClickListener for the ExpandableListView
//        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                showEditDeleteDialog(position);
//                return true;
//            }
//        });

        return view;
    }

    private void fetchCategoriesAndPopulateSpinner() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("categories");

        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categoryNames = new ArrayList<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("name").getValue(String.class);
                    if (categoryName != null) {
                        categoryNames.add(categoryName);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("InventoryFragment", "Failed to fetch categories: " + databaseError.getMessage());
            }
        });
    }

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

                CustomExpandableListAdapter customExpandableListAdapter = new CustomExpandableListAdapter(getContext(), items);
                expandableListView.setAdapter(customExpandableListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

//    private void showEditDeleteDialog(final int position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_edit_delete, null);
//        builder.setView(dialogView);
//
//        Button btnEdit = dialogView.findViewById(R.id.btnEdit);
//        Button btnDelete = dialogView.findViewById(R.id.btnDelete);
//
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//
//        btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Edit item at position: " + position, Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Delete item at position: " + position, Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//    }
}
