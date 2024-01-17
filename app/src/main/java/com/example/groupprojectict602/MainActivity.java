package com.example.groupprojectict602;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
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
                replaceFragment(new CategoryFragment());
            } else if (item.getItemId() == R.id.viewInventory) {
                replaceFragment(new InventoryFragment());
            }

            return true;
        });

        binding.fabAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDataDialog();
            }
        });
    }

//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_layout, fragment);
//        fragmentTransaction.commit();
//    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
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
        return monthNames[month - 1];
    }

    private void showAddDataDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.adddata_dialog, null);
        editTextBarcode = dialogView.findViewById(R.id.editTextBarcode);
        Button btnScanBarcode = dialogView.findViewById(R.id.btnScanBarcode);

        editTextBarcode.setEnabled(false);
        editTextBarcode.setFocusable(false);

        btnScanBarcode.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(false);
            integrator.setPrompt("Scan a barcode");
            integrator.initiateScan();
        });

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextQuantity = dialogView.findViewById(R.id.editTextQuantity);
        DatePicker datePickerExpiredDate = dialogView.findViewById(R.id.datePickerExpiredDate);

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
                        MainActivity.this,
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                displaySnackbar("Failed to fetch categories: " + databaseError.getMessage());
            }
        });

        dialogBuilder.setView(dialogView)
                .setTitle("Add Data")
                .setPositiveButton("Add", (dialog, which) -> {
                    String selectedCategory = spinnerCategory.getSelectedItem() != null ? spinnerCategory.getSelectedItem().toString() : null;

                    if (selectedCategory == null || selectedCategory.isEmpty()) {
                        displaySnackbar("Please add a category before adding data.");
                        return;
                    }

                    String name = editTextName.getText().toString().trim();
                    String quantity = editTextQuantity.getText().toString().trim();

                    if (selectedCategory.isEmpty() || name.isEmpty() || quantity.isEmpty() || scannedBarcode == null || scannedBarcode.isEmpty()) {
                        displaySnackbar("There is an empty field");
                        scannedBarcode = "";
                        return;
                    }

                    DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("items");
                    itemsRef.orderByChild("barcode").equalTo(scannedBarcode).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                displaySnackbar("Barcode already exists in the database!");
                                // Dismiss the dialog when barcode exists
                                if (alertDialog != null && alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                }
                            } else {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("items");

                                String key = databaseReference.push().getKey();

                                HashMap<String, Object> dataMap = new HashMap<>();
                                dataMap.put("category", selectedCategory);
                                dataMap.put("name", name);
                                dataMap.put("quantity", quantity);
                                dataMap.put("barcode", scannedBarcode);

                                int day = datePickerExpiredDate.getDayOfMonth();
                                int month = datePickerExpiredDate.getMonth() + 1;
                                int year = datePickerExpiredDate.getYear();

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
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            displaySnackbar("Error checking barcode: " + databaseError.getMessage());
                        }
                    });
                })
                .setNegativeButton("Close", (dialog, which) -> {
                    dialog.dismiss();
                });

        alertDialog = dialogBuilder.create();
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
                scannedBarcode = result.getContents();
                if (editTextBarcode != null) {
                    editTextBarcode.setText(scannedBarcode);
                } else {
                    displaySnackbar("editTextBarcode is null");
                }
            }
        }
    }

    private String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
}
