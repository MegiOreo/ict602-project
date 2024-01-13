//package com.example.groupprojectict602;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.Fragment;
//
//import android.text.InputType;
//import android.text.TextUtils;
//import android.view.ContextMenu;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//public class ProductsFragment extends Fragment {
//
//    private DatabaseReference itemsReference;
//    private TextView categoryCountTextView;
//    private ListView categoryListView;
//    private ArrayAdapter<String> adapter;
//    private Map<String, Integer> categoryCounts = new HashMap<>();
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        View view = inflater.inflate(R.layout.fragment_products, container, false);
////
////        // Initialize Firebase Database
////        itemsReference = FirebaseDatabase.getInstance().getReference().child("categories"); // Point to the 'categories' node
////
////        categoryCountTextView = view.findViewById(R.id.categoryCount);
////        categoryListView = view.findViewById(R.id.categoryListView);
////
////        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
////        categoryListView.setAdapter(adapter);
////
////        // Count categories from the 'categories' node
////        itemsReference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                if (dataSnapshot.exists()) {
////                    categoryCounts.clear(); // Clear previous category counts
////                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
////                        String categoryName = categorySnapshot.child("name").getValue(String.class);
////                        if (categoryName != null) {
////                            // Increment count for the category
////                            categoryCounts.put(categoryName, categoryCounts.getOrDefault(categoryName, 0) + 1);
////                        }
////                    }
////
////                    // Update the TextView with the count of unique categories
////                    categoryCountTextView.setText(String.valueOf(categoryCounts.size() + " categories available"));
////
////                    // Update ListView with categories and their counts
//////                    adapter.clear();
//////                    for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
//////                        String categoryName = entry.getKey();
//////                        int count = entry.getValue();
//////                        adapter.add(categoryName + ": " + count);  // Display in format: categoryName: count
//////                   }
////                } else {
////                    categoryCountTextView.setText("0");
////                    adapter.clear();
////                }
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////                Toast.makeText(getActivity(), "Error fetching categories!", Toast.LENGTH_SHORT).show();
////            }
////        });
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_products, container, false);
//
//        DatabaseReference categoriesReference = FirebaseDatabase.getInstance().getReference().child("categories");
//        itemsReference = FirebaseDatabase.getInstance().getReference().child("items"); // Point to the 'items' node
//
//        categoryCountTextView = view.findViewById(R.id.categoryCount);
//        categoryListView = view.findViewById(R.id.categoryListView);
//
//        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
//        categoryListView.setAdapter(adapter);
//
//        registerForContextMenu(categoryListView);
//
//
//        // Fetch all categories from the 'categories' node
//        categoriesReference.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    categoryCounts.clear(); // Clear previous category counts
//
//                    // Initialize category counts to 0 for all categories
//                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//                        String categoryName = categorySnapshot.child("name").getValue(String.class);
//                        if (categoryName != null) {
//                            categoryCounts.put(categoryName, 0); // Initialize count to 0
//                        }
//                    }
//
//                    // Count categories from the 'items' node
//                    itemsReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot itemDataSnapshot) {
//                            if (itemDataSnapshot.exists()) {
//                                for (DataSnapshot itemSnapshot : itemDataSnapshot.getChildren()) {
//                                    String itemCategory = itemSnapshot.child("category").getValue(String.class);
//                                    if (itemCategory != null && categoryCounts.containsKey(itemCategory)) {
//                                        // Increment count for the category
//                                        categoryCounts.put(itemCategory, categoryCounts.get(itemCategory) + 1);
//                                    }
//                                }
//                            }
//
//                            // Update the TextView with the count of unique categories
//                            categoryCountTextView.setText(String.valueOf(categoryCounts.size() + " categories available"));
//
//                            // Update ListView with categories and their counts
//                            adapter.clear();
//                            for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
//                                String categoryName = entry.getKey();
//                                int count = entry.getValue();
//                                adapter.add(categoryName + ": " + count);  // Display in format: categoryName: count
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            Toast.makeText(getActivity(), "Error fetching items!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    categoryCountTextView.setText("0 Category Available");
//                    adapter.clear();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Error fetching categories!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        View view = inflater.inflate(R.layout.fragment_products, container, false);
////
////        // Initialize Firebase Database
////        itemsReference = FirebaseDatabase.getInstance().getReference().child("items");
////
////        categoryCountTextView = view.findViewById(R.id.categoryCount);
////        categoryListView = view.findViewById(R.id.categoryListView);
////
////        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
////        categoryListView.setAdapter(adapter);
////
////        // Count categories from the 'items' node
////        itemsReference.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                if (dataSnapshot.exists()) {
////                    categoryCounts.clear(); // Clear previous category counts
////                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
////                        String category = itemSnapshot.child("category").getValue(String.class);
////                        if (category != null) {
////                            // Increment count for the category
////                            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
////                        }
////                    }
////
////                    // Update the TextView with the count of unique categories
////                    categoryCountTextView.setText(String.valueOf(categoryCounts.size() + " categories available"));
////
////                    // Update ListView with categories and their counts
////                    adapter.clear();
////                    for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
////                        String categoryName = entry.getKey();
////                        int count = entry.getValue();
////                        adapter.add(categoryName + ": " + count);
////                    }
////                } else {
////                    categoryCountTextView.setText("0");
////                    adapter.clear();
////                }
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////                Toast.makeText(getActivity(), "Error fetching categories!", Toast.LENGTH_SHORT).show();
////            }
////        });
//
//        //category db store
//        //DatabaseReference categoriesReference = FirebaseDatabase.getInstance().getReference().child("categories");
//
//        // Set OnClickListener for the addCategory ImageView
//        view.findViewById(R.id.addCategory).setOnClickListener(v -> {
//            // Create and show a dialog to get user input for the category name
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle("Add New Category");
//
//            // Set up the input
//            final EditText input = new EditText(getActivity());
//            input.setInputType(InputType.TYPE_CLASS_TEXT);
//            builder.setView(input);
//
//            // Set up the buttons
//            builder.setPositiveButton("Add", (dialog, which) -> {
//                String categoryName = input.getText().toString().trim();
//
//                // Check if the category name is not empty
//                if (!TextUtils.isEmpty(categoryName)) {
//                    // Create a new category object with name and formatted dateAdded
//                    Map<String, Object> categoryMap = new HashMap<>();
//                    categoryMap.put("name", categoryName);
//
//                    // Format the current timestamp into "dd MMMM yyyy" format
//                    String formattedDate = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(new Date());
//                    categoryMap.put("dateAdded", formattedDate);
//
//                    // Add the new category to the categories node in Firebase Realtime Database
//                    categoriesReference.push().setValue(categoryMap)
//                            .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Category added successfully!", Toast.LENGTH_SHORT).show())
//                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to add category!", Toast.LENGTH_SHORT).show());
//                } else {
//                    Toast.makeText(getActivity(), "Please enter a category name!", Toast.LENGTH_SHORT).show();
//                }
//            });
//            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//
//            builder.show();
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.setHeaderTitle("Options");
//        menu.add(0, v.getId(), 0, "Edit");
//        menu.add(0, v.getId(), 0, "Delete");
//    }
//
//    private void editCategoryName(String currentName) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Edit Category Name");
//
//        // Set up the input
//        final EditText input = new EditText(getActivity());
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        input.setText(currentName);  // Pre-fill the current name
//        builder.setView(input);
//
//        // Set up the buttons
//        builder.setPositiveButton("Save", (dialog, which) -> {
//            String newName = input.getText().toString().trim();
//
//            if (!TextUtils.isEmpty(newName) && !newName.equals(currentName)) { // Check if newName is not empty and different from the current name
//                // Fetch the category reference from Firebase
//                DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
//                categoryRef.orderByChild("name").equalTo(currentName).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//                                // Update the category name
//                                categorySnapshot.getRef().child("name").setValue(newName)
//                                        .addOnSuccessListener(aVoid -> {
//                                            Toast.makeText(getActivity(), "Category updated successfully!", Toast.LENGTH_SHORT).show();
//                                            dialog.dismiss(); // Close the dialog after successful update
//                                        })
//                                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update category!", Toast.LENGTH_SHORT).show());
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getActivity(), "Error updating category!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                if (newName.equals(currentName)) {
//                    Toast.makeText(getActivity(), "Please enter a new category name different from the current one!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "Please enter a new category name!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//
//        // Create the dialog from the builder
//        AlertDialog dialog = builder.create();
//
//        // Clear the input text box when the dialog is shown
//        dialog.setOnShowListener(dialogInterface -> input.setText(""));
//
//        // Show the dialog
//        dialog.show();
//    }
//
//
////    private void editCategoryName(String currentName) {
////        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////        builder.setTitle("Edit Category Name");
////
////        // Set up the input
////        final EditText input = new EditText(getActivity());
////        input.setInputType(InputType.TYPE_CLASS_TEXT);
////        input.setText(currentName);  // Pre-fill the current name
////        builder.setView(input);
////
////        // Set up the buttons
////        builder.setPositiveButton("Save", (dialog, which) -> {
////            String newName = input.getText().toString().trim();
////
////            if (!TextUtils.isEmpty(newName)) {
////                // Fetch the category reference from Firebase
////                DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
////                categoryRef.orderByChild("name").equalTo(currentName).addListenerForSingleValueEvent(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                        if (dataSnapshot.exists()) {
////                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
////                                // Update the category name
////                                categorySnapshot.getRef().child("name").setValue(newName)
////                                        .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Category updated successfully!", Toast.LENGTH_SHORT).show())
////                                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update category!", Toast.LENGTH_SHORT).show());
////                            }
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError databaseError) {
////                        Toast.makeText(getActivity(), "Error updating category!", Toast.LENGTH_SHORT).show();
////                    }
////                });
////            } else {
////                Toast.makeText(getActivity(), "Please enter a new category name!", Toast.LENGTH_SHORT).show();
////            }
////        });
////
////        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
////
////        builder.show();
////    }
//
//    public void deleteDataFunction(String selectedCategory) {
//        // Show a confirmation dialog before deleting the category
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Confirm Deletion");
//        builder.setMessage("Are you sure you want to delete the category: " + selectedCategory + "?");
//
//        // Set up the buttons
//        builder.setPositiveButton("Delete", (dialog, which) -> {
//            // Delete the selected category from the Firebase Realtime Database
//            DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
//            DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
//
//            // Delete from 'categories' node
//            categoriesRef.orderByChild("name").equalTo(selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//                            categorySnapshot.getRef().removeValue()
//                                    .addOnSuccessListener(aVoid -> {
//                                        Toast.makeText(getActivity(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
//
//                                        // Now delete corresponding items from 'items' node
//                                        itemsRef.orderByChild("category").equalTo(selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
//                                                if (itemSnapshot.exists()) {
//                                                    for (DataSnapshot item : itemSnapshot.getChildren()) {
//                                                        item.getRef().removeValue(); // Delete each item
//                                                    }
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                Toast.makeText(getActivity(), "Error deleting items!", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        Toast.makeText(getActivity(), "Failed to delete category!", Toast.LENGTH_SHORT).show();
//                                    });
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), "Category not found!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(getActivity(), "Error deleting category!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//
//        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
//
//        // Create and show the confirmation dialog
//        builder.show();
//    }
//
//
//    //start edit
//
////    public void deleteDataFunction(String selectedCategory) {
////        // Show a confirmation dialog before deleting the category
////        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////        builder.setTitle("Confirm Deletion");
////        builder.setMessage("Are you sure you want to delete the category: " + selectedCategory + "?");
////
////        // Set up the buttons
////        builder.setPositiveButton("Delete", (dialog, which) -> {
////            // Delete the selected category from the Firebase Realtime Database
////            DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
////            categoryRef.orderByChild("name").equalTo(selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
////                @Override
////                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    if (dataSnapshot.exists()) {
////                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
////                            categorySnapshot.getRef().removeValue()
////                                    .addOnSuccessListener(aVoid -> {
////                                        Toast.makeText(getActivity(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
////                                        dialog.dismiss(); // Close the dialog after successful deletion
////                                    })
////                                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to delete category!", Toast.LENGTH_SHORT).show());
////                        }
////                    }
////                }
////
////                @Override
////                public void onCancelled(@NonNull DatabaseError databaseError) {
////                    Toast.makeText(getActivity(), "Error deleting category!", Toast.LENGTH_SHORT).show();
////                }
////            });
////
////
//////            Task<void> mTask = categoryRef.removeValue();
//////            mTask.addOnSuccessListener(new onSuccessListener<Void>(){
//////                @Override
//////                public void onSuccess(Void aVoid) {
//////
//////                }
//////            })
////        });
////
////        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
////
////        // Create and show the confirmation dialog
////        builder.show();
////    }
//
//
//
////    @Override
////    public boolean onContextItemSelected(MenuItem item) {
////        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
////
////        String selectedCategory = adapter.getItem(info.position);
////
////        if (item.getTitle().equals("Edit")) {
////            // Call the editCategoryName function
////            editCategoryName(selectedCategory);
////        } else if (item.getTitle().equals("Delete")) {
////            //deleteDataFunction(selectedCategory);
////            // Show a confirmation dialog before deleting the category
////            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////            builder.setTitle("Confirm Deletion");
////            builder.setMessage("Are you sure you want to delete the category: " + selectedCategory + "?");
////
////            // Set up the buttons
////            builder.setPositiveButton("Delete", (dialog, which) -> {
////                // Delete the selected category from the Firebase Realtime Database
////                DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
////                categoryRef.orderByChild("name").equalTo(selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                        if (dataSnapshot.exists()) {
////                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
////                                // Get the category ID
////                                String categoryId = categorySnapshot.getKey();
////
////                                // Delete the category using its ID
////                                assert categoryId != null;
////                                categoryRef.child(categoryId).removeValue()
////                                        .addOnSuccessListener(aVoid -> {
////                                            Toast.makeText(getActivity(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
////                                        })
////                                        .addOnFailureListener(e -> {
////                                            Toast.makeText(getActivity(), "Failed to delete category!", Toast.LENGTH_SHORT).show();
////                                        });
////                            }
////
//////                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//////                                categorySnapshot.getRef().removeValue()
//////                                        .addOnSuccessListener(aVoid -> {
//////                                            Toast.makeText(getActivity(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
//////                                            dialog.dismiss(); // Close the dialog after successful deletion
//////                                        })
//////                                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to delete category!", Toast.LENGTH_SHORT).show());
//////                            }
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError databaseError) {
////                        Toast.makeText(getActivity(), "Error deleting category!", Toast.LENGTH_SHORT).show();
////                    }
////                });
////            });
////
////            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
////
////            // Create and show the confirmation dialog
////            builder.show();
////        }
////
////        return true;
////    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        String selectedCategory = adapter.getItem(info.position);
//
//        if (item.getTitle().equals("Edit")) {
//            // Call the editCategoryName function
//            editCategoryName(selectedCategory);
//        } else if (item.getTitle().equals("Delete")) {
//            deleteDataFunction(selectedCategory);
//
//            // Show a confirmation dialog before deleting the category
////            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////            builder.setTitle("Confirm Deletion");
////            builder.setMessage("Are you sure you want to delete the category: " + selectedCategory + "?");
////
////            // Set up the buttons
////            builder.setPositiveButton("Delete", (dialog, which) -> {
////                // Delete the selected category from the Firebase Realtime Database
////                DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
////                categoryRef.orderByChild("name").equalTo(selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                        if (dataSnapshot.exists()) {
////                            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
////                                // Remove the category
////                                categorySnapshot.getRef().removeValue()
////                                        .addOnSuccessListener(aVoid -> {
////                                            Toast.makeText(getActivity(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
////                                        })
////                                        .addOnFailureListener(e -> {
////                                            Toast.makeText(getActivity(), "Failed to delete category!", Toast.LENGTH_SHORT).show();
////                                        });
////                            }
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError databaseError) {
////                        Toast.makeText(getActivity(), "Error deleting category!", Toast.LENGTH_SHORT).show();
////                    }
////                });
////            });
////
////            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
////
////            // Create and show the confirmation dialog
////            builder.show();
//        }
//
//        return true;
//    }
//
//
//
//
//
//
//
//
//}