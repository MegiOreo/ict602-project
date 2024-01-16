/*
 * Copyright [Year] [Your Company/Organization Name]
 *
 * All rights reserved.
 *
 * The text, images, graphics, sound files, animation files, video files, and their
 * arrangement on this inventory application are subject to copyright and other intellectual
 * property protection. These materials may not be copied for commercial use or distribution,
 * nor may these materials be modified or reposted to other sites without the express written
 * permission of [Your Company/Organization Name].
 *
 * This inventory app and its content are protected by copyright law and international treaties.
 * Unauthorized reproduction or distribution of this app, or any portion of it, may result in
 * severe civil and criminal penalties, and will be prosecuted to the maximum extent possible
 * under the law.
 *
 * For permissions to use copyrighted materials from this app, please contact:
 *
 * [Your Contact Information]
 * [Your Company/Organization Name]
 * [Your Address]
 * [Your Email]
 * [Your Phone Number]
 *
 * [Optional: Any additional information or disclaimers you wish to include]
 */

package com.example.groupprojectict602;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DevelopersDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_developers_details);

        // Developer 1 details
        setDeveloperDetails(R.id.developer1_name, "Amirul Hakimi bin Asmadi", R.id.developer1_student_id, "2023660566", R.id.developer1_class_group, "CDCS2703B");

        // Developer 2 details
        setDeveloperDetails(R.id.developer2_name, "Fabian Harrison Anak Thomas Tarang", R.id.developer2_student_id, "2023622802", R.id.developer2_class_group, "CDCS2703B");

        // Developer 3 details
        setDeveloperDetails(R.id.developer3_name, "Alif Zikri bin Azharie", R.id.developer3_student_id, "2022868168", R.id.developer3_class_group, "CDCS2703B");

        // Developer 4 details
        setDeveloperDetails(R.id.developer4_name, "Muhammad Shafiq Izzuan bin Ahmad", R.id.developer4_student_id, "2020830692", R.id.developer4_class_group, "CDCS2703B");

        // Visit Website Button
        Button visitWebsiteButton = findViewById(R.id.visit_website_button);
        visitWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace "https://www.example.com" with your actual website URL
                String websiteUrl = "https://stackoverflow.com/questions/15526805/two-main-activities-in-androidmanifest-xml";

                // Create an implicit intent to open a web browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                startActivity(intent);
            }
        });

        // Set Copyright Text
        TextView copyrightTextView = findViewById(R.id.copyright_text);
        copyrightTextView.setText("Collaboratively Developed by Hakimi, Fabian, Alif, Shafiq. © 2024. All rights reserved.");

        Log.d("DevelopersDetails", "DevelopersDetailsActivity onCreate called");
    }

    private void setDeveloperDetails(int nameResId, String name, int idResId, String id, int groupResId, String group) {
        TextView nameTextView = findViewById(nameResId);
        TextView idTextView = findViewById(idResId);
        TextView groupTextView = findViewById(groupResId);

        nameTextView.setText("Name: " + name);
        idTextView.setText("Student ID: " + id);
        groupTextView.setText("Class Group: " + group);
    }

    public void onEzInventoryInfoClick(View view) {
        // Toggle visibility of app details when EzInventory Info is clicked
        LinearLayout appDetailsLayout = findViewById(R.id.app_details_layout);
        if (appDetailsLayout.getVisibility() == View.VISIBLE) {
            appDetailsLayout.setVisibility(View.GONE);
        } else {
            appDetailsLayout.setVisibility(View.VISIBLE);
        }
    }
}
