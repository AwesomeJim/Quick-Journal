package com.jim.quickjournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddJournalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
            getSupportActionBar().setIcon(R.drawable.ic_edit_journal);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

    }
}
