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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jim.quickjournal.AppExecutors;
import com.jim.quickjournal.R;
import com.jim.quickjournal.db.JournalDatabase;
import com.jim.quickjournal.db.entity.JournalEntry;
import com.jim.quickjournal.viewmodel.JournalViewModel;
import com.jim.quickjournal.viewmodel.JournalViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class JournalDetailActivity extends AppCompatActivity implements View.OnClickListener {

  // Extra for the task ID to be received in the intent
  public static final String EXTRA_JOURNAL_ID = "extraJournalId";

  // Constant for date format
  private static final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm aa";

  // Date formatter
  private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

  TextInputEditText mJournalTitleEditText;
  TextInputEditText mJournalBodyEditText;
  TextView mDateView;
  MaterialButton btn_delete;
  MaterialButton btn_update;
  JournalDatabase mDb;

  JournalEntry mJournalEntry;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_journal_detail);
     mDb = JournalDatabase.getInstance(getApplicationContext());

         initViews();
    Intent intent = getIntent();
    if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
       int mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID,-1);
      JournalViewModelFactory modelFactory=new JournalViewModelFactory(mDb,mJournalId);
      JournalViewModel viewModel= ViewModelProviders.of(this,modelFactory).get(JournalViewModel.class);
         viewModel.getJournalEntryLiveData().observe(this, new Observer<JournalEntry>() {
           @Override public void onChanged(@Nullable JournalEntry jEntry) {
             //journalEntry.removeObserver(this);
             populateUI(jEntry);
             mJournalEntry=jEntry;
           }
         });


    }
  }

  /**
   * Initialize the activity Views
   */
  private void initViews()
  {
    mJournalTitleEditText=findViewById(R.id.editText_journal_title);
    mJournalBodyEditText=findViewById(R.id.editText_journal_body);
    mDateView=findViewById(R.id.textView_date);
    btn_delete=findViewById(R.id.button_delete);
    btn_update=findViewById(R.id.button_update);
    btn_update.setOnClickListener(this);
    btn_delete.setOnClickListener(this);
  }


  /**
   * populateUI would be called to populate the UI when in view mode
   *
   * @param journalEntry the Journal Entry to populate the UI
   */
  private void populateUI(JournalEntry journalEntry) {
    mJournalTitleEditText.setText(journalEntry.getTitle());
    mJournalBodyEditText.setText(journalEntry.getBody());
    mDateView.setText(dateFormat.format(journalEntry.getUpdatedOn()));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_journal_detail_activity, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id){
      case R.id.bar_edit_property:
       upDateJournal();
        break;
     default:
       break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClick(View view) {
    int id =view.getId();
    switch (id){
      case R.id.button_delete:
       deleteJournal();
        break;
      case R.id.button_update:
        upDateJournal();
        break;
    }

  }

  /**
   * Called when update Journal button is clicked
   */
  public void upDateJournal(){
    Intent intent = new Intent(JournalDetailActivity.this, AddJournalActivity.class);
    intent.putExtra(AddJournalActivity.EXTRA_JOURNAL_ID, mJournalEntry.getId());
    startActivity(intent);
  }

  /**
   * Prompts to Deletes a given journal
   * with a dialog
   */
  public void deleteJournal(){
    new AlertDialog.Builder(this)
        .setTitle("Confirm Deletion!")
        .setMessage("are you sure you want to delete this journal?")
        .setPositiveButton("No Cancel", null)
        .setNegativeButton("yes Delete", (dialog, which) -> AppExecutors.getInstance().diskIO().execute(() -> {
          mDb.journalDao().deleteJournal(mJournalEntry);
          finish();
        }))
        .setIcon(R.drawable.ic_delete)
        .show();
  }
}
