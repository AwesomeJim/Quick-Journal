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

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jim.quickjournal.R;
import com.jim.quickjournal.db.entity.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Adapter Responsible for Displaying the Journals to the Application UI
 */
public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

// Constant for date format
private static final String DATE_FORMAT = "d MMM yyyy HH:mm aa";
private static final String DATE_FORMAT_INIT = "EEE";

// Member variable to handle item clicks
final private ItemClickListener mItemClickListener;

// Class variables for the List that holds task data and the Context
private List<JournalEntry> mJournalEntries;
private Context mContext;

// Date formatter
private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
  private SimpleDateFormat dateFormatInit = new SimpleDateFormat(DATE_FORMAT_INIT, Locale.getDefault());

/**
 * Constructor for the TaskAdapter that initializes the Context.
 *
 * @param context  the current Context
 * @param listener the ItemClickListener
 */
JournalAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
        }

/**
 * Called when ViewHolders are created to fill a RecyclerView.
 *
 * @return A new JournalViewHolder that holds the view for each task
 */
@NonNull
@Override
public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
        .inflate(R.layout.item_journal_entry, parent, false);

        return new JournalViewHolder(view);
        }

/**
 * Called by the RecyclerView to display data at a specified position in the Cursor.
 *
 * @param holder   The ViewHolder to bind Cursor data to
 * @param position The position of the data in the Cursor
 */
@Override
public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        // Determine the values of the wanted data
        JournalEntry journalEntry = mJournalEntries.get(position);
        String title=journalEntry.getTitle();
        String body= journalEntry.getBody();
        String updatedOn = dateFormat.format(journalEntry.getUpdatedOn());
        GradientDrawable gradientDrawable=(GradientDrawable)holder.intial.getBackground();
        gradientDrawable.setColor(getRandomColor());
        //Set values
        holder.JournalTitleView.setText(title);
        holder.JournalBodyView.setText(body);
        holder.JournalDateView.setText(updatedOn);
        holder.intial.setText(dateFormatInit.format(journalEntry.getUpdatedOn()));
        }

  /**
   *
   * @return Returns the number of items to display.
   */
  @Override
public int getItemCount() {
        if (mJournalEntries == null) {
        return 0;
        }
        return mJournalEntries.size();
        }

  /**
   * @param journalEntries
   * When data changes, this method updates the list of journalEntries
   * and notifies the adapter to use the new values on it
   */
  public void setJournals(List<JournalEntry> journalEntries) {
        mJournalEntries =journalEntries;
        notifyDataSetChanged();
        }

public interface ItemClickListener {
    void onItemClickListener(int itemId);
}

  /**
   *
   * @return Ca random color which is used a background by
   * day textview
   */
  private int getRandomColor(){
    Random rnd = new Random();
    return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
  }


// Inner class for creating ViewHolders
class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Class variables for the task description and priority TextViews
    TextView JournalTitleView;
    TextView JournalBodyView;
    TextView JournalDateView;
    TextView intial;


  /**
     * Constructor for the TaskViewHolders.
     *
     * @param itemView The view inflated in onCreateViewHolder
     */
    JournalViewHolder(View itemView) {
        super(itemView);

        JournalTitleView = itemView.findViewById(R.id.textView_journal_title);
        JournalBodyView= itemView.findViewById(R.id.textView_journal_body);
        JournalDateView = itemView.findViewById(R.id.textView_journal_date);
        intial=itemView.findViewById(R.id.imageView_journal);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //get the Id of the Item Clicked
        int elementId = mJournalEntries.get(getAdapterPosition()).getId();
        mItemClickListener.onItemClickListener(elementId);
    }

}

}