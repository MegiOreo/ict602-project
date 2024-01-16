package com.example.groupprojectict602;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ListView listView;
    private Spinner dropdownSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("items");

        // Initialize views
        TextView greetingTextView = view.findViewById(R.id.greetingTextView);
        listView = view.findViewById(R.id.listView);
        dropdownSpinner = view.findViewById(R.id.dropdownSpinner);

        // AddCategory button
        ImageView addCategoryButton = view.findViewById(R.id.viewInfo);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a new screen (replace NewScreenActivity.class with your new activity class)
                Intent intent = new Intent(getActivity(), DevelopersDetailsActivity.class);
                startActivity(intent);
            }
        });

        // Inside your onCreateView method after setting up the spinner adapter
        dropdownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected category from the spinner
                String selectedCategory = parent.getItemAtPosition(position).toString();

                // Call fetchItemsForCategory with the selected category
                fetchItemsForCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no item is selected
            }
        });

        //Get the current hour of the day
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (currentHour >= 0 && currentHour < 12) {
            greetingTextView.setText("Good Morning");
        } else if (currentHour >= 12 && currentHour < 16) {
            greetingTextView.setText("Good Afternoon");
        } else if (currentHour >= 16 && currentHour < 21) {
            greetingTextView.setText("Good Evening");
        } else {
            greetingTextView.setText("Good Night");
        }

        fetchCategoriesAndPopulateSpinner();

        return view;
    }

    private void fetchCategoriesAndPopulateSpinner() {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("categories");

        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!isAdded()) {
                    // Fragment is not attached to the activity, do not proceed
                    return;
                }

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
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                if (isAdded()) {
                    dropdownSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if any
                Log.e("InventoryFragment", "Failed to fetch categories: " + databaseError.getMessage());
            }
        });
    }

    private void fetchItemsForCategory(String category) {
        if (!isAdded()) {
            // Fragment is not attached to the activity, do not proceed
            return;
        }

        // Query the Firebase database to get items of the selected category
        databaseReference.orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!isAdded()) {
                    // Fragment is not attached to the activity, do not proceed
                    return;
                }

                List<String> namesList = new ArrayList<>();

                // Convert the DataSnapshot to a list of items
                List<Item> items = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null && item.getDateAdded() != null) {
                        items.add(item);
                    }
                }

                // Sort the items based on the dateAdded in descending order
                Collections.sort(items, (item1, item2) -> {
                    // Assuming dateAdded is stored in the format "hh:mm a, dd MMMM yyyy"
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a, dd MMMM yyyy", Locale.getDefault());
                    try {
                        Date date1 = sdf.parse(item1.getDateAdded());
                        Date date2 = sdf.parse(item2.getDateAdded());
                        return date2.compareTo(date1); // For descending order
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0; // Default case
                    }
                });

                // Add the names of the latest items to the list
                for (int i = 0; i < Math.min(3, items.size()); i++) { // Limit to 3 items or the list size if less than 3
                    namesList.add(items.get(i).getName());
                }

                // Populate the ListView with the filtered items
                if (isAdded()) {
                    ArrayAdapter<String> namesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, namesList);
                    listView.setAdapter(namesAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("HomeFragment", "Firebase Error: " + databaseError.getMessage());
            }
        });
    }
}
