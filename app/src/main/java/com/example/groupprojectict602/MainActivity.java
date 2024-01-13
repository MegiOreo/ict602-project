package com.example.groupprojectict602;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormatSymbols;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.groupprojectict602.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private EditText editTextBarcode;
    private String scannedBarcode;

    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(binding.getRoot());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        FirebaseApp.initializeApp(this);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.searchItems) {
                replaceFragment(new SearchFragment());
            } else if (item.getItemId() == R.id.viewCategory) {
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
                //displaySnackbar("Add Data FAB Clicked");
                showAddDataDialog();
            }
        });

    }


    private void replaceFragment(Fragment fragment) {
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
        //EditText editTextBarcode = dialogView.findViewById(R.id.editTextBarcode);
        editTextBarcode = dialogView.findViewById(R.id.editTextBarcode);
        Button btnScanBarcode = dialogView.findViewById(R.id.btnScanBarcode);

        editTextBarcode.setEnabled(false);
        editTextBarcode.setFocusable(false);


        btnScanBarcode.setOnClickListener(v -> {
            // Initialize ZXing's intent integrator for scanning
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(false);  // Enable rotation
            integrator.setPrompt("Scan a barcode");
            integrator.initiateScan();
        });

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        DatePicker datePickerExpiredDate = dialogView.findViewById(R.id.datePickerExpiredDate);

//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.categories_array, android.R.layout.simple_spinner_item);
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        spinnerCategory.setAdapter(adapter);

        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("categories");

        // Fetch category names from Firebase
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categoryNames = new ArrayList<>();

                // Iterate through the categories and add names to the list
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("name").getValue(String.class);
                    if (categoryName != null) {
                        categoryNames.add(categoryName);
                    }
                }

                // Create an ArrayAdapter using the fetched category names
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                spinnerCategory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if any
                displaySnackbar("Failed to fetch categories: " + databaseError.getMessage());
            }
        });

        dialogBuilder.setView(dialogView)
                .setTitle("Add Data")
                .setPositiveButton("Add", (dialog, which) -> {
                    String selectedCategory = spinnerCategory.getSelectedItem().toString();
                    String name = editTextName.getText().toString().trim();
                    String quantity = editTextQuantity.getText().toString().trim();
//start
                    // Check if any of the required fields are empty
                    if (selectedCategory.isEmpty() || name.isEmpty() || quantity.isEmpty() || scannedBarcode == null || scannedBarcode.isEmpty()) {

                        displaySnackbar("There is empty field");
                        scannedBarcode = "";  // Make sure to handle this properly if necessary
                        return; // Exit the method if any field is null or empty
                    }

                    int day = datePickerExpiredDate.getDayOfMonth();
                    int month = datePickerExpiredDate.getMonth() + 1;
                    int year = datePickerExpiredDate.getYear();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items");

                    String key = databaseReference.push().getKey();

                    HashMap<String, Object> dataMap = new HashMap<>();
                    dataMap.put("category", selectedCategory);
                    dataMap.put("name", name);
                    dataMap.put("quantity", quantity);
                    dataMap.put("barcode", scannedBarcode);

                    String expiryDate = day + " " + getMonthName(month) + " " + year;
                    dataMap.put("expiryDate", expiryDate);

                    String currentDateAndTime = getCurrentDateAndTime();
                    dataMap.put("dateAdded", currentDateAndTime);

                    assert key != null;
                    databaseReference.child(key).setValue(dataMap)
                            .addOnSuccessListener(aVoid -> {
                                displaySnackbar("Data added successfully!");
                            })
                            .addOnFailureListener(e -> {
                                displaySnackbar("Failed to add data: " + e.getMessage());
                            });


                    //storeScannedBarcodeToFirebase(scannedBarcode);
                    addBarcodeToFirebase(scannedBarcode);
                })


//                .setNegativeButton("Close", (dialog, which) -> {
//                    // User clicked close, simply dismiss the dialog
//                    dialog.dismiss();
//                });

                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked close, simply dismiss the dialog
                        dialog.dismiss();
                    }
                });

        alertDialog = dialogBuilder.create();
//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                View view = ((AlertDialog) dialogInterface).getWindow().getDecorView().findViewById(android.R.id.content);
//                displaySnackbarAboveDialog(view, "Your message here.");
//            }
//        });


        //AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                displaySnackbar("Cancelled");
            } else {
                scannedBarcode = result.getContents();  // Set the value of scannedBarcode

                // Check if editTextBarcode is not null and set its text
                if (editTextBarcode != null) {
                    editTextBarcode.setText(scannedBarcode);
                } else {
                    displaySnackbar("editTextBarcode is null"); // Debugging check
                }

                storeScannedBarcodeToFirebase(scannedBarcode);  // Store the scanned barcode to Firebase
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() == null) {
//                displaySnackbar("Cancelled");
//            } else {
//                scannedBarcode = result.getContents();  // Set the value of scannedBarcode
//
//                // Check if editTextBarcode is not null and set its text
//                if (editTextBarcode != null) {
//                    editTextBarcode.setText(scannedBarcode);
//                } else {
//                    displaySnackbar("editTextBarcode is null"); // Debugging check
//                }
//            }
//        }
//    }


    // Method to get current date and time in a readable format
    private String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void storeScannedBarcodeToFirebase(String scannedBarcode) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("scannedItems");

        databaseReference.orderByChild("barcode").equalTo(scannedBarcode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Barcode already exists in the database
                    View dialogView = alertDialog.getWindow().getDecorView().findViewById(android.R.id.content);
                    //displaySnackbarAboveDialog(dialogView, "Barcode already exists in the database!");
                    displaySnackbar("Barcode already exists in the database!");
                    // Dismiss the dialog when barcode exists
                    alertDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                displaySnackbar("Error checking barcode: " + databaseError.getMessage());
            }
        });
    }


//    private void storeScannedBarcodeToFirebase(String scannedBarcode) {
//        // Create a reference to your Firebase database
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("scannedItems");
//
//        // Check if the scannedBarcode already exists in the database
//        databaseReference.orderByChild("barcode").equalTo(scannedBarcode).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Barcode already exists in the database
//                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                        @Override
//                        public void onShow(DialogInterface dialogInterface) {
//                            View view = ((AlertDialog) dialogInterface).getWindow().getDecorView().findViewById(android.R.id.content);
//                            displaySnackbarAboveDialog(view, "Barcode already exists in the database!");
//
//                            //displaySnackbar(view, "Barcode already exists in the database!");
//                            alertDialog.dismiss();
//                        }
//                    });
//                } else {
//                    // Barcode does not exist, so proceed to add it
//                    addBarcodeToFirebase(scannedBarcode);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle any errors
//                displaySnackbar("Error checking barcode: " + databaseError.getMessage());
//            }
//        });
//    }


//    private void storeScannedBarcodeToFirebase(String scannedBarcode) {
//        // Create a reference to your Firebase database
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("scannedItems");
//
//        // Check if the scannedBarcode already exists in the database
//        databaseReference.orderByChild("barcode").equalTo(scannedBarcode).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Barcode already exists in the database
//                    //displaySnackbarAboveDialog(binding.getRoot(), "Barcode already exists in the database!");
//                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                        @Override
//                        public void onShow(DialogInterface dialogInterface) {
//                            View view = ((AlertDialog) dialogInterface).getWindow().getDecorView().findViewById(android.R.id.content);
//                            displaySnackbarAboveDialog(view, "Barcode already exists in the database!");
//                        }
//                    });
//
//
//                } else {
//                    // Barcode does not exist, so proceed to add it
//                    addBarcodeToFirebase(scannedBarcode);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle any errors
//                displaySnackbar("Error checking barcode: " + databaseError.getMessage());
//            }
//        });
//    }

    private void addBarcodeToFirebase(String scannedBarcode) {
        // Create a reference to your Firebase database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("scannedItems");

        // Create a unique key for the new data entry
        String key = databaseReference.push().getKey();

        // Create a HashMap to store the scanned barcode data
        HashMap<String, Object> barcodeDataMap = new HashMap<>();
        barcodeDataMap.put("barcode", scannedBarcode);

        // Write the scanned barcode data to Firebase
        assert key != null;
        databaseReference.child(key).setValue(barcodeDataMap)
                .addOnSuccessListener(aVoid -> {
                    displaySnackbar("Scanned barcode data added successfully!");
                })
                .addOnFailureListener(e -> {
                    displaySnackbar("Failed to add scanned barcode data: " + e.getMessage());
                });
    }


}