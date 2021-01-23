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

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.jim.quickjournal.db.JournalDatabase;

public class JournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {
  private final JournalDatabase mdb;
  private final int mJournalId;

  public JournalViewModelFactory(JournalDatabase mdb, int mJournalId) {
    this.mdb = mdb;
    this.mJournalId = mJournalId;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    return (T) new JournalViewModel(mdb,mJournalId);
  }
}
