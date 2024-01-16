package com.example.groupprojectict602;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.groupprojectict602.R;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawer extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                int itemId = item.getItemId();
//                if (itemId == R.id.nav_item_developer1) {
//                    showToast("Developer 1 details");
//                    //showDevsDetails("Kimi", "2023");
//                } else if (itemId == R.id.nav_item_developer2) {
//                    showToast("Developer 2 details");
//                } else if (itemId == R.id.nav_item_developer3) {
//                    showToast("Developer 3 details");
//                } else if (itemId == R.id.nav_item_developer4) {
//                    showToast("Developer 4 details");
//                }
////                } else if (itemId == R.id.nav_item_copyright) {
////                    showToast("Copyright Statement");
////                } else if (itemId == R.id.nav_item_website) {
////                    showToast("Visit Website");
////                    //openWebsite();
////                }
//
//                drawerLayout.closeDrawers();
//                return true;
//            }
//        });

        // Set name and student ID in the header
//        View headerView = navigationView.getHeaderView(0);
//        TextView headerName = headerView.findViewById(R.id.header_name);
//        TextView headerStudentId = headerView.findViewById(R.id.header_student_id);a
//
//        headerName.setText("Developer");
//        headerStudentId.setText("Student ID: 12345");

        //showDevsDetails("Ict602", "CDCS2703B");
    }

//    private void showDevsDetails(String devName, String studId){
//        View headerView = navigationView.getHeaderView(0);
//        TextView headerName = headerView.findViewById(R.id.header_name);
//        TextView headerStudentId = headerView.findViewById(R.id.header_student_id);
//
//        //headerName.setText("Developer");
//        //headerStudentId.setText("Student ID: 12345");
//        headerName.setText(devName);
//        headerStudentId.setText(studId);
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void openWebsite() {
//        // Define the URL of the website you want to open
//        String websiteUrl = "https://stackoverflow.com/questions/15526805/two-main-activities-in-androidmanifest-xml"; // Replace with your website URL
//
//        // Create an Intent to open a web browser
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
//
//        // Check if there is a browser available to handle the Intent
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            showToast("No web browser found");
//        }
//    }

}
