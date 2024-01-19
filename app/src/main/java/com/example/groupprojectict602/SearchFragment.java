package com.example.groupprojectict602;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//start edit
public class SearchFragment extends Fragment {

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    ExpandableListView expandableListView;
    List<Item> itemList;
    CustomExpandableSearch customExpandableSearch;
    SearchView searchView;

    private Set<String> uniqueItemKeys = new HashSet<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        expandableListView = rootView.findViewById(R.id.expandableListView);
        searchView = rootView.findViewById(R.id.search);
        searchView.clearFocus();

        itemList = new ArrayList<>();  // Initialize itemList as an empty list

        customExpandableSearch = new CustomExpandableSearch(getActivity(), itemList);
        expandableListView.setAdapter(customExpandableSearch);

        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        return rootView;
    }

    public void searchList(String text) {
        itemList.clear();  // Clear existing items
        uniqueItemKeys.clear(); // Clear existing keys

        if (!text.isEmpty()) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        Item item = itemSnapshot.getValue(Item.class);
                        if (item != null &&
                                ((item.getBarcode() != null && item.getBarcode().toLowerCase().contains(text.toLowerCase())) ||
                                        (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase())))) {

                            String itemKey = itemSnapshot.getKey();

                            // Check if the item key is unique before adding
                            if (uniqueItemKeys.add(itemKey)) {
                                itemList.add(item);
                                Log.d("SearchFragment", "Added item with key: " + itemKey);
                            }
                        }
                    }
                    customExpandableSearch.notifyDataSetChanged();
                    Log.d("SearchFragment", "Number of items after search: " + itemList.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("SearchFragment", "Database error: " + error.getMessage());
                    // Handle error
                }
            });
        } else {
            customExpandableSearch.notifyDataSetChanged();
            Log.d("SearchFragment", "Number of items after empty search: " + itemList.size());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}