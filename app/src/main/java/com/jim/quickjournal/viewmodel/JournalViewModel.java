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
package com.jim.quickjournal.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.jim.quickjournal.db.JournalDatabase;
import com.jim.quickjournal.db.entity.JournalEntry;

public class JournalViewModel extends ViewModel {

  private LiveData<JournalEntry> journalEntryLiveData;

  public JournalViewModel(JournalDatabase mdb, int journalId) {
    journalEntryLiveData=mdb.journalDao().loadJournalById(journalId);
  }

  public LiveData<JournalEntry> getJournalEntryLiveData(){
    return  journalEntryLiveData;
  }
}