package com.example.groupprojectict602;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProductsFragment extends Fragment {

    private DatabaseReference itemsReference;
    private TextView categoryCountTextView;
    private ListView categoryListView;
    private ArrayAdapter<String> adapter;
    private Map<String, Integer> categoryCounts = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        // Initialize Firebase Database
        itemsReference = FirebaseDatabase.getInstance().getReference().child("items");

        categoryCountTextView = view.findViewById(R.id.categoryCount);
        categoryListView = view.findViewById(R.id.categoryListView);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        categoryListView.setAdapter(adapter);

        // Count categories from the 'items' node
        itemsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    categoryCounts.clear(); // Clear previous category counts
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        String category = itemSnapshot.child("category").getValue(String.class);
                        if (category != null) {
                            // Increment count for the category
                            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
                        }
                    }

                    // Update the TextView with the count of unique categories
                    categoryCountTextView.setText(String.valueOf(categoryCounts.size() + " categories available"));

                    // Update ListView with categories and their counts
                    adapter.clear();
                    for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
                        String categoryName = entry.getKey();
                        int count = entry.getValue();
                        adapter.add(categoryName + ": " + count);
                    }
                } else {
                    categoryCountTextView.setText("0");
                    adapter.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error fetching categories!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}