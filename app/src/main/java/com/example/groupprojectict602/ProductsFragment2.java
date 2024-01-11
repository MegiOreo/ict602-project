package com.example.groupprojectict602;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment2 extends Fragment {

    // Firebase Database reference
    private DatabaseReference mDatabase;
    private TextView categoryCountTextView;
    //private TextView categoryTextView;
    private ListView categoryListView;
    private ArrayAdapter<String> adapter;
    private List<String> categoriesList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("categories");

        categoryCountTextView = view.findViewById(R.id.categoryCount);
        //categoryTextView = view.findViewById(R.id.categoryTextView);
        categoryListView = view.findViewById(R.id.categoryListView);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categoriesList);
        categoryListView.setAdapter(adapter);

        ImageView addCategoryButton = view.findViewById(R.id.addCategory);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });

        //count text view
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int count = (int) dataSnapshot.getChildrenCount();
                    categoryCountTextView.setText(String.valueOf(count) + " category");
                } else {
                    categoryCountTextView.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error fetching categories!", Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoriesList.clear(); // Clear previous categories
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    if (category != null) {
                        categoriesList.add(category.name); // Add category name to the list
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter about data change
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error fetching categories!", Toast.LENGTH_SHORT).show();
            }
        });

//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<String> categories = new ArrayList<>();  // Create a list to store category names
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String categoryName = snapshot.getKey();  // Assuming the category names are keys in this case
//                    categories.add(categoryName);
//                }
//                // Pass categories list to the dialog method
//                binding.fabAddData.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        displaySnackbar("Add Data FAB Clicked");
//                        showAddDataDialog(categories);  // Pass the list to the dialog
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Error fetching categories!", Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;
    }

    private AlertDialog dialog;

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.addcat_dialog, null);

        EditText editTextCategoryName = dialogView.findViewById(R.id.editTextCategoryName);
        Button btnAddCategory = dialogView.findViewById(R.id.btnAddCategory);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        // Add a flag to prevent multiple clicks
        final boolean[] isButtonClickable = {true};

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButtonClickable[0]) {
                    isButtonClickable[0] = false; // Disable the button
                    String categoryName = editTextCategoryName.getText().toString().trim();
                    if (!categoryName.isEmpty()) {
                        // Check if the category already exists
                        checkCategoryExists(categoryName);
                    } else {
                        Toast.makeText(getActivity(), "Please enter category name!", Toast.LENGTH_SHORT).show();
                    }

                    // Re-enable the button after a delay (e.g., 2 seconds)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isButtonClickable[0] = true; // Enable the button after delay
                        }
                    }, 2000); // 2000 milliseconds = 2 seconds
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when cancel button is clicked
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        builder.setView(dialogView);
        dialog = builder.create(); // Assign the created dialog to the class-level variable
        dialog.show();
    }


    private void checkCategoryExists(final String categoryName) {
        mDatabase.orderByChild("name").equalTo(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getActivity(), "Category already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // Add category to Firebase Database if it doesn't exist
                    addCategoryToFirebase(categoryName);
                    Toast.makeText(getActivity(), "Category added successfully!", Toast.LENGTH_SHORT).show();
                    // Close the dialog
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Category added successfully!")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error checking category!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCategoryToFirebase(String categoryName) {
        String categoryId = mDatabase.push().getKey();
        Category category = new Category(categoryId, categoryName);
        mDatabase.child(categoryId).setValue(category);
    }

    // Define a Category class if you don't have one
    public static class Category {
        public String id;
        public String name;

        public Category() {
            // Default constructor required for calls to DataSnapshot.getValue(Category.class)
        }

        public Category(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
