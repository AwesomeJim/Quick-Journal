package com.jim.quickjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.jim.quickjournal.data.JournalAdapter;
import com.jim.quickjournal.data.database.JournalDatabase;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener {



    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    // Member variables for the adapter, RecyclerView and database
    private RecyclerView mRecyclerView;
    private JournalAdapter  mAdapter;
    private JournalDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setIcon(R.drawable.ic_journal);
        }

        mDb=JournalDatabase.getInstance(getApplicationContext());

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerView);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new JournalAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAddJournalActivity=new Intent(MainActivity.this, AddJournalActivity.class);
                startActivity(startAddJournalActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
    public void onItemClickListener(int itemId) {
        // Launch AddTaskActivity adding the itemId as an extra in the intent
    }
}
