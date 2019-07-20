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
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import com.jim.quickjournal.db.JournalDatabase;
import com.jim.quickjournal.db.entity.JournalEntry;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

  private LiveData<List<JournalEntry>> journalEntryLiveData;
  public MainViewModel(@NonNull Application application) {
    super(application);
    JournalDatabase journalDatabase=JournalDatabase.getInstance(this.getApplication());
    journalEntryLiveData=journalDatabase.journalDao().loadAllJournals();
  }
  public LiveData<List<JournalEntry>> getJournalEntryLiveData(){
    return journalEntryLiveData;
  }
}