package com.jim.quickjournal.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.jim.quickjournal.R;
import com.jim.quickjournal.db.JournalDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener,NavigationView.OnNavigationItemSelectedListener {

    // Constant for logging
   // private static final String TAG = MainActivity.class.getSimpleName();

  private JournalAdapter  mAdapter;
    private JournalDatabase mDb;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Toolbar toolbar;
    String loginMode;
    String loginDetails;
    ImageView photo;
    Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);
        mDb=JournalDatabase.getInstance(getApplicationContext());
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        if(user!=null){
            loadGoogleUserDetails();
            initViews();
        }else {
            Intent intent= new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }



    }
public void loadGoogleUserDetails(){
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
        photo=headerLayout.findViewById(R.id.user_profile_image_view);
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


    /**
            * This method is called after this activity has been paused or restarted.
            * Often, this is after new data has been inserted through an AddJournalsActivity,
            * so this re-queries the database data for any changes.
            */
    @Override
    protected void onResume() {
        super.onResume();
        // Call the adapter's setTasks method using the result
        // of the loadAllTasks method from the taskDao
        mAdapter.setJournals(mDb.journalDao().loadAllJournals());
    }

    @Override
    public void onItemClickListener(final int itemId) {
        Intent intent = new Intent(MainActivity.this, JournalDetailActivity.class);
        intent.putExtra(AddJournalActivity.EXTRA_JOURNAL_ID, itemId);
        startActivity(intent);
    }
}
