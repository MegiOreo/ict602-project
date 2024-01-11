package com.example.groupprojectict602;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.icu.text.DateFormatSymbols;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.groupprojectict602.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(binding.getRoot());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        FirebaseApp.initializeApp(this);

//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            switch ( item.getItemId()) {
//                case R.id.home:
//                    replaceFragment(new HomeFragment());
//                    break;
//
//                case R.id.delete:
//                    replaceFragment(new DeleteFragment());
//                    break;
//
//                case R.id.viewProduct:
//                    replaceFragment(new ProductsFragment());
//                    break;
//
//                case R.id.viewInventory:
//                    replaceFragment(new InventoryFragment());
//                    break;
//            }
//
//            return true;
//        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.delete) {
                replaceFragment(new DeleteFragment());
            } else if (item.getItemId() == R.id.viewProduct) {
                replaceFragment(new ProductsFragment());
            } else if (item.getItemId() == R.id.viewInventory) {
                replaceFragment(new InventoryFragment());
            }

            return true;
        });

        binding.fabAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a simple Snackbar as an example when FAB is clicked
                displaySnackbar("Add Data FAB Clicked");
                showAddDataDialog();
            }
        });

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Initialize binding
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Inflate the menu for BottomNavigationView
//        binding.bottomNavigationView.inflateMenu(R.menu.bottom_menu);
//
//        // Set default fragment
//        replaceFragment(new HomeFragment());
//        binding.bottomNavigationView.setBackground(null);
//
//        // Set item selected listener for BottomNavigationView
////        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
////            switch (item.getItemId()) {
////                case R.id.home:
////                    replaceFragment(new HomeFragment());
////                    break;
////
////                case R.id.delete:
////                    replaceFragment(new DeleteFragment());
////                    break;
////
////                case R.id.viewProduct:
////                    replaceFragment(new ProductsFragment());
////                    break;
////
////                case R.id.viewInventory:
////                    replaceFragment(new InventoryFragment());
////                    break;
////            }
////
////            return true;
////        });
//
//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.home) {
//                replaceFragment(new HomeFragment());
//            } else if (item.getItemId() == R.id.delete) {
//                replaceFragment(new DeleteFragment());
//            } else if (item.getItemId() == R.id.viewProduct) {
//                replaceFragment(new ProductsFragment());
//            } else if (item.getItemId() == R.id.viewInventory) {
//                replaceFragment(new InventoryFragment());
//            }
//
//            return true;
//        });
//
//    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void displaySnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    private String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[month - 1]; // Subtract 1 since array is 0-indexed
    }


    private void showAddDataDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.adddata_dialog, null);

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        DatePicker datePickerExpiredDate = dialogView.findViewById(R.id.datePickerExpiredDate);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerCategory.setAdapter(adapter);

        dialogBuilder.setView(dialogView)
                .setTitle("Add Data")
                .setPositiveButton("Add", (dialog, which) -> {
                    String selectedCategory = spinnerCategory.getSelectedItem().toString();
                    String name = editTextName.getText().toString().trim();
                    String quantity = editTextQuantity.getText().toString().trim();
                    int day = datePickerExpiredDate.getDayOfMonth();
                    int month = datePickerExpiredDate.getMonth() + 1;
                    int year = datePickerExpiredDate.getYear();

                    // Create a reference to your Firebase database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items");

                    // Create a unique key for the new data entry
                    String key = databaseReference.push().getKey();

                    // Create a HashMap to store the data
                    HashMap<String, Object> dataMap = new HashMap<>();
                    dataMap.put("category", selectedCategory);
                    dataMap.put("name", name);
                    dataMap.put("quantity", quantity);
                    //dataMap.put("expiryDate", day + "/" + month + "/" + year);
                    // Get the month name using the getMonthName method
                    String expiryDate = day + " " + getMonthName(month) + " " + year;
                    dataMap.put("expiryDate", expiryDate);

                    String currentDateAndTime = getCurrentDateAndTime();
                    dataMap.put("dateAdded", currentDateAndTime);

                    // Write data to Firebase
                    databaseReference.child(key).setValue(dataMap)
                            .addOnSuccessListener(aVoid -> {
                                displaySnackbar("Data added successfully!");
                            })
                            .addOnFailureListener(e -> {
                                displaySnackbar("Failed to add data: " + e.getMessage());
                            });
                })

                .setNegativeButton("Close", (dialog, which) -> {
                    // User clicked close, simply dismiss the dialog
                    dialog.dismiss();
                });


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private String getMonthAdded(int month) {
        DateFormatSymbols dfs = new DateFormatSymbols();
        return dfs.getMonths()[month - 1];
    }

    // Method to get current date and time in a readable format
    private String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

//    private void showAddDataDialog() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.adddata_dialog, null);
//
//        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
//        EditText editTextName = dialogView.findViewById(R.id.editTextName);
//        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
//        DatePicker datePickerExpiredDate = dialogView.findViewById(R.id.datePickerExpiredDate);
//
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.categories_array, android.R.layout.simple_spinner_item);
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        spinnerCategory.setAdapter(adapter);
//
//        dialogBuilder.setView(dialogView)
//                .setTitle("Add Data")
//                .setPositiveButton("Add", (dialog, which) -> {
//                    String selectedCategory = spinnerCategory.getSelectedItem().toString();
//                    String name = editTextName.getText().toString().trim();
//                    String quantity = editTextQuantity.getText().toString().trim();
//                    int day = datePickerExpiredDate.getDayOfMonth();
//                    int month = datePickerExpiredDate.getMonth() + 1; // Month is 0-based
//                    int year = datePickerExpiredDate.getYear();
//
//                    String inputData = "Category: " + selectedCategory +
//                            "\nName: " + name +
//                            "\nQuantity: " + quantity +
//                            "\nExpired Date: " + day + "/" + month + "/" + year;
//
//                    displaySnackbar("Data added:\n" + inputData);
//                })
//                .setNegativeButton("Cancel", (dialog, which) -> {
//                    // User clicked cancel, do nothing
//                });
//
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//    }


//    private void showAddDataDialog() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.adddata_dialog, null); // Corrected the layout name
//
//        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
//        EditText editTextName = dialogView.findViewById(R.id.editTextName);
//        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
//        DatePicker datePickerExpiredDate = dialogView.findViewById(R.id.datePickerExpiredDate);
//
//        dialogBuilder.setView(dialogView)
//                .setTitle("Add Data")
//                .setPositiveButton("Add", (dialog, which) -> {
//                    String selectedCategory = spinnerCategory.getSelectedItem().toString();
//                    String name = editTextName.getText().toString().trim();
//                    String quantity = editTextQuantity.getText().toString().trim();
//                    int day = datePickerExpiredDate.getDayOfMonth();
//                    int month = datePickerExpiredDate.getMonth() + 1; // Month is 0-based
//                    int year = datePickerExpiredDate.getYear();
//
//                    // TODO: Add your logic to save the data
//                    String inputData = "Category: " + selectedCategory +
//                            "\nName: " + name +
//                            "\nQuantity: " + quantity +
//                            "\nExpired Date: " + day + "/" + month + "/" + year;
//
//                    displaySnackbar("Data added:\n" + inputData);
//                })
//                .setNegativeButton("Cancel", (dialog, which) -> {
//                    // User clicked cancel, do nothing
//                });
//
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//    }

//    private void handleAddCategory() {
//        // For now, displaying a simple dialog as an example
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Add Category");
//        builder.setMessage("Functionality to add a category can be implemented here.");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // You can add your logic here if needed
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }


//    private void showAddDataDialog() {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.adddata_dialog, null); // Create a layout for your dialog
//
//        EditText editTextData = dialogView.findViewById(R.id.editTextData); // Add EditText to get user input
//
//        dialogBuilder.setView(dialogView)
//                .setTitle("Add Data") // Set dialog title
//                .setPositiveButton("Add", (dialog, which) -> {
//                    String inputData = editTextData.getText().toString();
//                    // TODO: Add your logic to save the inputData
//                    displaySnackbar("Data added: " + inputData); // Display Snackbar with the added data
//                })
//                .setNegativeButton("Cancel", (dialog, which) -> {
//                    // User clicked cancel, do nothing
//                });
//
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//    }
}