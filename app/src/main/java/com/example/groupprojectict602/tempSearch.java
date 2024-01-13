//package com.example.groupprojectict602;
//
//import android.os.Bundle;
//
//import androidx.appcompat.widget.SearchView;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ExpandableListView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//// ... Your existing imports ...
//
//public class SearchFragment extends Fragment {
//
//    SearchView searchView;
//    ExpandableListView expandableListView;
//    List<Item> originalItemList = new ArrayList<>(); // Original list from Firebase
//    List<Item> filteredItemList = new ArrayList<>(); // Filtered list for search results
//    CustomExpandableListAdapter adapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_search, container, false);
//
//        searchView = view.findViewById(R.id.search);
//        expandableListView = view.findViewById(R.id.expandableListView);
//
//        // Initialize the adapter with the original list
//        adapter = new CustomExpandableListAdapter(getContext(), originalItemList);
//        expandableListView.setAdapter(adapter);
//
//        // Fetch data from Firebase and populate originalItemList
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("items");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                originalItemList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Item item = snapshot.getValue(Item.class);
//                    originalItemList.add(item);
//                }
//                Log.d("SearchFragment", "Original Item List Size: " + originalItemList.size());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle error
//                Log.e("SearchFragment", "Error fetching items", databaseError.toException());
//            }
//        });
//
//        //        databaseReference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                originalItemList.clear();
////                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                    Item item = snapshot.getValue(Item.class);
////                    originalItemList.add(item);
////                }
////                adapter.notifyDataSetChanged();
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////                // Handle error
////            }
////        });
//
//        // Implement search functionality
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            //            @Override
////            public boolean onQueryTextChange(String newText) {
////                filterItemList(newText);
////                return true;
////            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.d("SearchFragment", "Query changed to: " + newText); // Add this line for debugging
//                //filterItemList(newText);
//                resetAndDisplayMatchingItems(newText);
//                return true;
//            }
//
//        });
//
//        return view;
//    }
//
//    //    private void filterItemList(String query) {
////        filteredItemList.clear();
////        boolean isItemFound = false; // Flag to check if the item is found
////
////        for (Item item : originalItemList) {
////            // Filter based on the barcode
////            if (item.getBarcode() != null && item.getBarcode().toLowerCase().contains(query.toLowerCase())) {
////                filteredItemList.add(item);
////                isItemFound = true; // Set the flag to true if the item is found
////            }
////        }
////
////        // Update the adapter with the filtered list
////        adapter.setFilteredList(filteredItemList);
////        adapter.notifyDataSetChanged();
////
////        // Display a Toast message based on whether the item is found or not
////        if (isItemFound) {
////            Toast.makeText(getContext(), "Item(s) found!", Toast.LENGTH_SHORT).show();
////        } else {
////            Toast.makeText(getContext(), "No item(s) found for the given barcode!", Toast.LENGTH_SHORT).show();
////        }
////    }
//    private void resetAndDisplayMatchingItems(String query) {
//        originalItemList.clear(); // Clear the original list
//
//        for (Item item : filteredItemList) {
//            // If the item's barcode matches the search query, add it back to the original list
//            if (item.getBarcode() != null && item.getBarcode().toLowerCase().contains(query.toLowerCase())) {
//                originalItemList.add(item);
//            }
//        }
//
//        // Notify the adapter of the changes
//        adapter.notifyDataSetChanged();
//    }
//
//
//
//}
//
//
////public class SearchFragment extends Fragment {
////
////    SearchView searchView;
////    ExpandableListView expandableListView;
////    List<Item> itemList = new ArrayList<>(); // Declare itemList at class level
////    CustomExpandableListAdapter adapter;
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////
////        View view = inflater.inflate(R.layout.fragment_search, container, false);
////
////        searchView = view.findViewById(R.id.search);
////        expandableListView = view.findViewById(R.id.expandableListView); // Initialize expandableListView
////
////        // Assuming you will implement a custom adapter for ExpandableListView
////        adapter = new CustomExpandableListAdapter(getContext(), itemList);
////        expandableListView.setAdapter(adapter);
////
////        searchView.clearFocus();
////
////        // Fetch data from Firebase and populate itemList
////        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("items");
////        databaseReference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                itemList.clear(); // Clear existing list before adding items
////                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                    Item item = snapshot.getValue(Item.class);
////                    itemList.add(item);
////                }
////                adapter.notifyDataSetChanged(); // Notify the adapter about the data change
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////                // Handle error
////            }
////        });
////
////        // Implementing search functionality
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String newText) {
////                searchItemList(newText);
////                return true;
////            }
////        });
////
////        return view;
////    }
////
////    private void searchItemList(String query) {
////        List<Item> filteredList = new ArrayList<>();
////        for (Item item : itemList) {
////            // Here, you can modify the condition based on which field you want to search against (e.g., barcode, name, etc.)
////            if (item.getBarcode().toLowerCase().contains(query.toLowerCase())) {
////                filteredList.add(item);
////            }
////        }
////        // Update your adapter's list with the filtered list and notify data change
////        adapter.setFilteredList(filteredList);
////        adapter.notifyDataSetChanged();
////    }
////}
//
//
////public class SearchFragment extends Fragment {
////
////    SearchView searchView;
////    ExpandableListView expandableListView;
////    List<Item> itemList = new ArrayList<>(); // Declare itemList at class level
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////
////        View view = inflater.inflate(R.layout.fragment_search, container, false);
////
////        searchView = view.findViewById(R.id.search);
////        expandableListView = view.findViewById(R.id.expandableListView); // Initialize expandableListView
////
////        searchView.clearFocus();
////
////        // Assuming you will implement a custom adapter for ExpandableListView
////        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(getContext(), itemList);
////        expandableListView.setAdapter(adapter);
////
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String newText) {
////                Log.d("SearchFragment", "Query Text: " + newText);
////                filter(newText, adapter);
////                return true;
////            }
////
////        });
////
////        // Fetch data from Firebase and populate itemList
////        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("items");
////        databaseReference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                itemList.clear(); // Clear existing list before adding items
////                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                    Item item = snapshot.getValue(Item.class);
////                    itemList.add(item);
////                }
////                adapter.notifyDataSetChanged(); // Notify the adapter about the data change
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////                // Handle error
////            }
////        });
////
////        return view;
////    }
////    private void filter(String query, CustomExpandableListAdapter adapter) {
////        List<Item> filteredList = new ArrayList<>();
////
////        String lowercaseQuery = query.toLowerCase(); // Convert query text to lowercase
////
////        for (Item item : itemList) {
////            if (item.getBarcode().toLowerCase().contains(lowercaseQuery)) { // Convert barcode to lowercase for comparison
////                filteredList.add(item);
////            }
////        }
////
////        // Update your adapter's list with the filtered list and notify data change
////        adapter.setFilteredList(filteredList);
////        adapter.notifyDataSetChanged();
////    }
//
//
////    private void filter(String query, CustomExpandableListAdapter adapter) {
////        List<Item> filteredList = new ArrayList<>();
////
////        for (Item item : itemList) {
////            if (item.getBarcode().contains(query)) {
////                filteredList.add(item);
////            }
////        }
////
////        // Update your adapter's list with the filtered list and notify data change
////        adapter.setFilteredList(filteredList);
////        adapter.notifyDataSetChanged();
////    }
////}
