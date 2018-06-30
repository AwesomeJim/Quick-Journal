package com.jim.quickjournal;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.jim.quickjournal.data.JournalAdapter;
import com.jim.quickjournal.data.JournalEntry;
import com.jim.quickjournal.data.database.JournalDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener,NavigationView.OnNavigationItemSelectedListener {

    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    // Member variables for the adapter, RecyclerView and database
    private RecyclerView mRecyclerView;
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
         toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        System.out.println(profile.getProviderId());
        if (profile.getProviderId().equals("google.com")) {
            // UID specific to the provider
            String uid = profile.getUid();
            // Name, email address, and profile photo Url
            loginMode= profile.getDisplayName();
            loginDetails = profile.getEmail();
            photoUrl = profile.getPhotoUrl();

        } else if (profile.getProviderId().equals("email")) {
            // UID specific to the provider
            String uid = profile.getUid();
            // Name, email address, and profile photo Url
            loginDetails= profile.getEmail();
            loginMode=profile.getProviderId();
        }
        else if (profile.getProviderId().equals("phone")) {
            // UID specific to the provider
            String uid = profile.getUid();
            // Name, email address, and profile photo Url
            loginDetails = profile.getPhoneNumber();
            loginMode = profile.getProviderId();

        }
    }


}
    public void initViews(){
        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerView);
        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new JournalAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        //Floating Action Button for adding a Journal entry
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAddJournalActivity=new Intent(MainActivity.this, AddJournalActivity.class);
                startActivity(startAddJournalActivity);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
            * This method is called after this activity has been paused or restarted.
            * Often, this is after new data has been inserted through an AddTaskActivity,
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
        final JournalEntry entry=mDb.journalDao().loadJournalById(itemId);
        new AlertDialog.Builder(this)
                .setTitle(entry.getTitle())
                .setMessage(entry.getBody())
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, AddJournalActivity.class);
                        intent.putExtra(AddJournalActivity.EXTRA_JOURNAL_ID, itemId);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDb.journalDao().deleteJournal(entry);
                    }
                })
                .setIcon(R.drawable.ic_journal_entry)
                .setNeutralButton("Ok", null)
                .show();
    }
}
