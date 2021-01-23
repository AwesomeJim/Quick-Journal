
/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jim.quickjournal.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.jim.quickjournal.R;
import com.jim.quickjournal.db.JournalDatabase;
import com.jim.quickjournal.db.entity.JournalEntry;
import com.jim.quickjournal.viewmodel.MainViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener,NavigationView.OnNavigationItemSelectedListener {


//Activity Member Variables
  private JournalAdapter  mAdapter;
    private JournalDatabase mDb;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Toolbar toolbar;
    String loginMode;
    String loginDetails;
    CircleImageView photo;
    Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize the AppDatabase
        mDb=JournalDatabase.getInstance(getApplicationContext());

        //Initializes the Firebase instance
        mAuth=FirebaseAuth.getInstance();

        //check if the user is login
        user=mAuth.getCurrentUser();
        if(user!=null){
          //User is logged in get their details and initialize the views and Load Journals
            loadGoogleUserDetails();
            initViews();
            setUpViewModel();
        }else {
          //User Not Logged In
            Intent intent= new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

  /**
   * Get the details of the Logged in User
   */
  public void loadGoogleUserDetails(){
    /**
     * Check which method/Provider a user Used to login
     */
    for (UserInfo profile : user.getProviderData()) {
        switch (profile.getProviderId()) {
            case "google.com": {

                // Name, email address, and profile photo Url
                loginMode = profile.getDisplayName();
                loginDetails = profile.getEmail();
                photoUrl = profile.getPhotoUrl();

                break;
            }
            case "firebase": {
                // Name, email address, and profile photo Url if available
                loginDetails = profile.getEmail();
                loginMode = profile.getDisplayName();
                break;
            }
            case "phone": {
                // Name, email address, and profile photo Url if its available
                loginDetails = profile.getPhoneNumber();
                loginMode = profile.getProviderId();

                break;
            }
        }
    }


}
    public void initViews(){
        // Set the RecyclerView to its corresponding view
      RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new JournalAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        //Floating Action Button for adding a Journal entry
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAddJournalActivity=new Intent(MainActivity.this, AddJournalActivity.class);
                startActivity(startAddJournalActivity);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        photo=headerLayout.findViewById(R.id.profile_image);
        TextView loginM=headerLayout.findViewById(R.id.login_method);
        loginM.setText(loginMode);
        TextView details=headerLayout.findViewById(R.id.user_details);
        details.setText(loginDetails);
        Picasso.get()
            .load(photoUrl)
            .into(photo);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.nav_logout){
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setUpViewModel(){
      MainViewModel viewModel= new ViewModelProvider(this).get(MainViewModel.class);
     viewModel.getJournalEntryLiveData().observe(this, new Observer<List<JournalEntry>>() {
        @Override public void onChanged(@Nullable List<JournalEntry> journalEntries) {
          mAdapter.setJournals(journalEntries);
        }
      });
    }

    @Override
    public void onItemClickListener(final int itemId) {
        Intent intent = new Intent(MainActivity.this, JournalDetailActivity.class);
        intent.putExtra(AddJournalActivity.EXTRA_JOURNAL_ID, itemId);
        startActivity(intent);
    }
}
