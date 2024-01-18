package com.example.groupprojectict602;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CategoryFragment extends Fragment {
    private DatabaseReference categoriesReference;
    private DatabaseReference itemsReference;
    private TextView categoryCountTextView;
    private ListView categoryListView;
    private ArrayAdapter<String> adapter;
    private Map<String, Integer> categoryCounts = new HashMap<>();

    public CategoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        //DatabaseReference categoriesReference = FirebaseDatabase.getInstance().getReference().child("categories");
        categoriesReference = FirebaseDatabase.getInstance().getReference().child("categories");


        categoryCountTextView = view.findViewById(R.id.categoryCount);
        categoryListView = view.findViewById(R.id.categoryListView);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        categoryListView.setAdapter(adapter);

        registerForContextMenu(categoryListView);

        // Fetch all categories from the 'categories' node
        categoriesReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    adapter.clear(); // Clear previous categories

                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        String categoryName = categorySnapshot.child("name").getValue(String.class);
                        if (categoryName != null) {
                            adapter.add(categoryName);  // Add category name to the adapter
                        }
                    }

                    // Update the TextView with the count of categories
                    categoryCountTextView.setText(String.valueOf(adapter.getCount() + " categories available"));
                } else {
                    categoryCountTextView.setText("0 Category Available");
                    adapter.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error fetching categories!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set OnClickListener for the addCategory ImageView
        view.findViewById(R.id.addCategory).setOnClickListener(v -> {
            // Create and show a dialog to get user input for the category name
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add New Category");

            // Set up the input
            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Add", (dialog, which) -> {
                String categoryName = input.getText().toString().trim();

                // Check if the category name is not empty
                if (!TextUtils.isEmpty(categoryName)) {
                    // Check if the category already exists
                    categoriesReference.orderByChild("name").equalTo(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Category with the same name already exists
                                Toast.makeText(getActivity(), "Category already exists!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Create a new category object with name and formatted dateAdded
                                Map<String, Object> categoryMap = new HashMap<>();
                                categoryMap.put("name", categoryName);

                                // Format the current timestamp into "dd MMMM yyyy" format
                                String formattedDate = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(new Date());
                                categoryMap.put("dateAdded", formattedDate);

                                // Add the new category to the categories node in Firebase Realtime Database
                                categoriesReference.push().setValue(categoryMap)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Category added successfully!", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to add category!", Toast.LENGTH_SHORT).show());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getActivity(), "Error checking category!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please enter a category name!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        Button deleteAllButton = view.findViewById(R.id.deleteAllCategoriesButton);
        deleteAllButton.setOnClickListener(v -> {;
            deleteAllCategoriesAndItems();
        });

        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Options");
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
    }

    private boolean isCategoryUpdated = false;

    private void editCategoryName(String currentName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Category Name");

        // Set up the input
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(currentName);  // Pre-fill the current name
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = input.getText().toString().trim();

            if (!TextUtils.isEmpty(newName) && !newName.equals(currentName)) {
                // Check if the new name already exists
                categoriesReference.orderByChild("name").equalTo(newName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Category with the same name already exists
                            Toast.makeText(getActivity(), "Category already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Fetch the category reference from Firebase
                            //DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference().child("categories");
                            DatabaseReference categoryRef = categoriesReference;

                            categoryRef.orderByChild("name").equalTo(currentName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                                            // Update the category name
                                            categorySnapshot.getRef().child("name").setValue(newName)
                                                    .addOnSuccessListener(aVoid -> {
                                                        isCategoryUpdated = true;
                                                        Toast.makeText(getActivity(), "Category updated successfully!", Toast.LENGTH_SHORT).show();

                                                        // Update items node with the new category name
                                                        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
                                                        itemsRef.orderByChild("category").equalTo(currentName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
                                                                if (itemSnapshot.exists()) {
                                                                    for (DataSnapshot item : itemSnapshot.getChildren()) {
                                                                        item.getRef().child("category").setValue(newName)
                                                                                .addOnSuccessListener(aVoid1 -> {
                                                                                    if (isCategoryUpdated) {
                                                                                        Toast.makeText(getActivity(), "Item updated with new category!", Toast.LENGTH_SHORT).show();
                                                                                        isCategoryUpdated = false; // Reset the flag to false after displaying the toast
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(e -> {
                                                                                    Toast.makeText(getActivity(), "Failed to update items with new category!", Toast.LENGTH_SHORT).show();

                                                                                });
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                Toast.makeText(getActivity(), "Error updating items!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                        dialog.dismiss(); // Close the dialog after successful update
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        isCategoryUpdated = false;
                                                        Toast.makeText(getActivity(), "Failed to update category!", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getActivity(), "Error updating category!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "Error checking category!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                if (newName.equals(currentName)) {
                    Toast.makeText(getActivity(), "Please enter a new category name different from the current one!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please enter a new category name!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Create the dialog from the builder
        AlertDialog dialog = builder.create();

        // Clear the input text box when the dialog is shown
        dialog.setOnShowListener(dialogInterface -> input.setText(""));

        // Show the dialog
        dialog.show();
    }



    public void deleteDataFunction(String selectedCategory) {
        // Show a confirmation dialog before deleting the category
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete the category: " + selectedCategory + "? This will also delete items within the selected category and cannot be undone");

        // Set up the buttons
        builder.setPositiveButton("Delete", (dialog, which) -> {
            DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
            DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
            categoriesRef.orderByChild("name").equalTo(selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //if (dataSnapshot.exists()) {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        categorySnapshot.getRef().removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getActivity(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();

                                    // Delete corresponding items from 'items' node
                                    itemsRef.orderByChild("category").equalTo(selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
                                            if (itemSnapshot.exists()) {
                                                for (DataSnapshot item : itemSnapshot.getChildren()) {
                                                    String itemBarcode = item.child("barcode").getValue(String.class);

                                                    // Delete the item
                                                    item.getRef().removeValue()
                                                            .addOnSuccessListener(aVoid2 -> {
                                                                Toast.makeText(getActivity(), "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Toast.makeText(getActivity(), "Failed to delete item!", Toast.LENGTH_SHORT).show();
                                                            });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Toast.makeText(getActivity(), "Error deleting items!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Failed to delete category!", Toast.LENGTH_SHORT).show();
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Error deleting category!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Create and show the confirmation dialog
        builder.show();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        String selectedCategory = adapter.getItem(info.position);

        if (item.getTitle().equals("Edit")) {
            // Call the editCategoryName function
            editCategoryName(selectedCategory);
        } else if (item.getTitle().equals("Delete")) {
            deleteDataFunction(selectedCategory);
        }

        return true;
    }
    private void deleteAllCategoriesAndItems() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");

        // Check if there are categories
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Generate a random text for confirmation
                    String randomText = generateRandomText();

                    // Display the random text to the user
                    showRandomTextDialog(randomText);
                } else {
                    // No categories found, show a message
                    Toast.makeText(requireContext(), "No categories found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Error checking categories!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String generateRandomText() {
        // Implement your logic to generate a random text (e.g., using UUID)
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void showRandomTextDialog(String randomText) {
        // Show a dialog with the generated random text
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Random Text for Confirmation");
        builder.setMessage("To proceed with deletion, please enter the following random text:\n\n" + randomText);

        // Set up the input
        final EditText inputRandomText = new EditText(requireContext());
        inputRandomText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(inputRandomText);

        // Set up the buttons
        builder.setPositiveButton("Proceed", (dialog, which) -> {
            // Get the text entered by the user
            String enteredText = inputRandomText.getText().toString().trim();

            // Check if the entered text matches the generated random text
            if (enteredText.equals(randomText)) {
                // User entered correct text, proceed with deletion
                performDeletion();
            } else {
                // Incorrect text entered, show a message
                Toast.makeText(requireContext(), "Incorrect random text. Deletion canceled.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void performDeletion() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");

        // Delete all categories
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                        String categoryName = categorySnapshot.child("name").getValue(String.class);

                        // Delete corresponding items from 'items' node
                        itemsRef.orderByChild("category").equalTo(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
                                if (itemSnapshot.exists()) {
                                    for (DataSnapshot item : itemSnapshot.getChildren()) {
                                        // Delete the item
                                        item.getRef().removeValue()
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(requireContext(), "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(requireContext(), "Failed to delete item!", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(requireContext(), "Error deleting items!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Delete the category
                        categorySnapshot.getRef().removeValue();
                    }
                    Toast.makeText(requireContext(), "All categories and corresponding items deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "No categories found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Error deleting categories!", Toast.LENGTH_SHORT).show();
            }
        });
    }





}