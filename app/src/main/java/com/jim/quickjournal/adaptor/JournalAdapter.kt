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
package com.jim.quickjournal.adaptor

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.jim.quickjournal.R
import com.jim.quickjournal.adaptor.JournalAdapter.JournalViewHolder
import com.jim.quickjournal.databinding.ItemJournalEntryBinding
import com.jim.quickjournal.db.entity.JournalEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Adapter Responsible for Displaying the Journals to the Application UI
 */

class JournalAdapter @Inject constructor() : RecyclerView.Adapter<JournalViewHolder>() {
    // Class variables for the List that holds task data and the Context
    private var mJournalEntries: List<JournalEntry>? = null

    private var ctx: Context? = null
    private var mOnItemClickListener: ((view: View, contact: JournalEntry, pos: Int) -> Unit)? =
        null


    /**
     * Sets on item click listener.
     *
     * @param mItemClickListener the m item click listener
     */
    fun setOnItemClickListener(mItemClickListener: (Any, Any, Any) -> Unit) {
        mOnItemClickListener = mItemClickListener
    }


    /**
     * Instantiates a new Adapter bills.
     *
     * @param context the context
     * @param items   the items
     */
    // Provide a suitable constructor (depends on the kind of dataset)
    fun setJournalsList(context: Context?, items: List<JournalEntry>) {
        mJournalEntries = items
        ctx = context
    }

    // Date formatter
    private val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    private val dateFormatInit = SimpleDateFormat(DATE_FORMAT_INIT, Locale.getDefault())

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new JournalViewHolder that holds the view for each task
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val binding = ItemJournalEntryBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return JournalViewHolder(binding)
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        // Determine the values of the wanted data
        holder.onBind(mJournalEntries!![position])
        setAnimation(holder.itemView, position)
    }

    /**
     *
     * @return Returns the number of items to display.
     */
    override fun getItemCount(): Int {
        return if (mJournalEntries == null) {
            0
        } else mJournalEntries!!.size
    }


    /**
     *
     * @return Ca random color which is used a background by
     * day textview
     */
    private val randomColor: Int
        get() {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }

    // Inner class for creating ViewHolders
    inner class JournalViewHolder(private val binding: ItemJournalEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(journalEntry: JournalEntry) = with(binding) {
            val updatedOn = dateFormat.format(journalEntry.updatedOn)
            val gradientDrawable = textViewJournal.background as GradientDrawable
            gradientDrawable.setColor(randomColor)
            textViewJournalTitle.text = journalEntry.title
            textViewJournalBody.text = journalEntry.body
            textViewJournalDate.text = updatedOn
            textViewJournal.text = dateFormatInit.format(journalEntry.updatedOn)
            itemView.setOnClickListener {
                mOnItemClickListener?.invoke(
                    view,
                    mJournalEntries!![adapterPosition],
                    adapterPosition
                )
            }
        }
    }

    companion object {
        // Constant for date format
        private const val DATE_FORMAT = "d MMM yyyy HH:mm aa"
        private const val DATE_FORMAT_INIT = "EEE"
    }

    /**
     * Here is the key method to apply the animation
     */
    private var lastPosition = -1

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation =
                AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}