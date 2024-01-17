package com.example.groupprojectict602;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.UUID;

public class InventoryFragment extends Fragment {

    private Spinner spinnerCategory;
    private DatabaseReference databaseReference;
    private ExpandableListView expandableListView;
    private CustomExpandableListAdapter customExpandableListAdapter;

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

        Button deleteAllButton = view.findViewById(R.id.deleteAllButton);
        deleteAllButton.setOnClickListener(v -> {
            // Check if there are categories
            if (spinnerCategory.getCount() > 0) {
                String selectedCategory = spinnerCategory.getSelectedItem().toString();

                // Check if there are items for the selected category
                if (customExpandableListAdapter != null && customExpandableListAdapter.getGroupCount() > 0) {
                    deleteAllItemsForCategory(selectedCategory);
                } else {
                    Toast.makeText(requireContext(), "No items to delete for the selected category", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "No categories available", Toast.LENGTH_SHORT).show();
            }
        });



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
                // Handle errors
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

                customExpandableListAdapter = new CustomExpandableListAdapter(getContext(), items);
                expandableListView.setAdapter(customExpandableListAdapter);

                // Set OnItemLongClickListener for the ExpandableListView
                expandableListView.setOnItemLongClickListener((parent, view, position, id) -> {
                    showOptionsDialog(items.get(position));
                    return true;
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void showOptionsDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Options");
        builder.setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    showEditDialog(item);
                    break;
                case 1:
                    showDeleteDialog(item);
                    break;
            }
        });
        builder.show();
    }

    private void showDeleteDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete the item: " + item.getName() + "? This cannot be undone");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            String selectedCategory = spinnerCategory.getSelectedItem().toString();
            deleteItemsForCategory(selectedCategory, item.getBarcode());
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Item");

        View editItemView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit, null);
        builder.setView(editItemView);

        EditText editName = editItemView.findViewById(R.id.editTextName);
        EditText editQuantity = editItemView.findViewById(R.id.editTextQuantity);
        EditText editExpiryDate = editItemView.findViewById(R.id.editTextExpiryDate);

        // Set the current values
        editName.setText(item.getName());
        editQuantity.setText(item.getQuantity());
        editExpiryDate.setText(item.getExpiryDate());

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Get the edited values
            String editedName = editName.getText().toString().trim();
            String editedQuantity = editQuantity.getText().toString().trim();
            String editedExpiryDate = editExpiryDate.getText().toString().trim();

            // Update the item in the database
            updateItem(item, editedName, editedQuantity, editedExpiryDate);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateItem(Item item, String editedName, String editedQuantity, String editedExpiryDate) {
        // Check if the input fields are not empty
        if (editedName.isEmpty() || editedQuantity.isEmpty() || editedExpiryDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please provide all the required information", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference itemRef = databaseReference.child(item.getBarcode());

        item.setName(editedName);
        item.setQuantity(editedQuantity);
        item.setExpiryDate(editedExpiryDate);

        itemRef.setValue(item)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to update item!", Toast.LENGTH_SHORT).show();
                });
    }


    private void deleteItemsForCategory(String selectedCategory, String barcode) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");

        itemsRef.orderByChild("barcode").equalTo(barcode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
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
                Toast.makeText(requireContext(), "Error deleting item!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllItemsForCategory(String selectedCategory) {
        // Generate a random text for confirmation
        String randomText = generateRandomText();

        // Display the random text to the user
        showRandomTextDialog(randomText, selectedCategory);
    }

    private String generateRandomText() {
        // Implement your logic to generate a random text (e.g., using UUID)
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void showRandomTextDialog(String randomText, String selectedCategory) {
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
                performDeletion(selectedCategory);
            } else {
                // Incorrect text entered, show a message
                Toast.makeText(requireContext(), "Incorrect random text. Deletion canceled.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void performDeletion(String selectedCategory) {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");

        // Delete all items in the selected category
        itemsRef.orderByChild("category").equalTo(selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        // Delete all items with the selected category
                        itemSnapshot.getRef().removeValue();
                    }
                    Toast.makeText(requireContext(), "All items with category " + selectedCategory + " deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "No items found for the selected category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Error deleting items!", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void deleteAllItemsForCategory(String selectedCategory) {
//        // First confirmation dialog
//        AlertDialog.Builder firstConfirmationBuilder = new AlertDialog.Builder(requireContext());
//        firstConfirmationBuilder.setTitle("Confirmation");
//        firstConfirmationBuilder.setMessage("This will delete all items in the category: " + selectedCategory + ". Proceed?");
//
//        firstConfirmationBuilder.setPositiveButton("Yes", (firstDialog, firstWhich) -> {
//            // Second confirmation dialog
//            AlertDialog.Builder secondConfirmationBuilder = new AlertDialog.Builder(requireContext());
//            secondConfirmationBuilder.setTitle("Confirm Deletion");
//            secondConfirmationBuilder.setMessage("Are you sure you want to proceed? This cannot be undone.");
//
//            secondConfirmationBuilder.setPositiveButton("Delete", (secondDialog, secondWhich) -> {
//                DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference().child("items");
//
//                itemsRef.orderByChild("category").equalTo(selectedCategory).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
//                                // Delete all items with the selected category
//                                itemSnapshot.getRef().removeValue();
//                            }
//                            Toast.makeText(requireContext(), "All items with category " + selectedCategory + " deleted successfully!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(requireContext(), "No items found for the selected category", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(requireContext(), "Error deleting items!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            });
//
//            secondConfirmationBuilder.setNegativeButton("Cancel", (secondDialog, secondWhich) -> secondDialog.dismiss());
//
//            secondConfirmationBuilder.show();
//
//            firstDialog.dismiss();
//        });
//
//        firstConfirmationBuilder.setNegativeButton("Cancel", (firstDialog, firstWhich) -> firstDialog.dismiss());
//
//        firstConfirmationBuilder.show();
//    }

}
