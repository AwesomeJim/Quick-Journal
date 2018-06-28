package com.jim.quickjournal;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.quickjournal.data.JournalAdapter;
import com.jim.quickjournal.data.JournalEntry;
import com.jim.quickjournal.data.database.JournalDatabase;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity implements View.OnClickListener {


    // Extra for the task ID to be received in the intent
    public static final String EXTRA_JOURNAL_ID = "extraJournalId";

    //Extra for the task ID to be received after rotation
    public static final String INSTANCE_JOURNAL_ID = "instanceJournalId";

    //Constant for default task id to be used when not in update mode
    private static final int DEFAULT_JOURNAL_ID = -1;

    //Constant for logging
    private static final String TAG = AddJournalActivity.class.getSimpleName();

    // Fields for views
   TextInputEditText mJournalTitleEditText;
   TextInputEditText mJournalBodyEditText;
   TextView mDateView;
   MaterialButton btn_Cancel;
   MaterialButton btn_Save;


    private int mJournalId = DEFAULT_JOURNAL_ID;

    //Member variable for the Database
    private JournalDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
        initViews();

        // Initialize member variable for the data base
        mDb = JournalDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)) {
            mJournalId= savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            btn_Save.setText(R.string.update_button);
            if (mJournalId == DEFAULT_JOURNAL_ID) {
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);
                JournalEntry journalEntry = mDb.journalDao().loadJournalById(mJournalId);
                populateUI(journalEntry);
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
       mJournalTitleEditText=findViewById(R.id.editText_journal_title);
       mJournalBodyEditText=findViewById(R.id.editText_journal_body);
       mDateView=findViewById(R.id.textView_date);
       btn_Cancel=findViewById(R.id.button_cancel);
       btn_Save=findViewById(R.id.button_save);
       btn_Save.setOnClickListener(this);
       btn_Cancel.setOnClickListener(this);
    }

    @Override  //Handle the OnclickListeners of the views
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.button_cancel:
                 finish();
                break;
            case R.id.button_save:
                onSaveButtonClicked();
                break;
                default:
                    break;

        }
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new Journal Entry data into the underlying database.
     */
    public void onSaveButtonClicked() {
        String title= mJournalTitleEditText.getText().toString();
        String body=mJournalBodyEditText.getText().toString();
        Date date = new Date();
        JournalEntry journalEntry = new JournalEntry(title, body, date);

        // insert the task only if mJournalId matches DEFAULT_JOURNAL_ID
        // Otherwise update it
        // call finish in any case
        if (mJournalId == DEFAULT_JOURNAL_ID) {
            // insert new task
            mDb.journalDao().insertJournal(journalEntry);
        } else {
            //update task
            journalEntry.setId(mJournalId);
            mDb.journalDao().updateJournal(journalEntry);
        }
        finish();
    }


    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param journalEntry the Journal Entry to populate the UI
     */
    private void populateUI(JournalEntry journalEntry) {
        mJournalTitleEditText.setText(journalEntry.getTitle());
        mJournalBodyEditText.setText(journalEntry.getBody());

    }
}
