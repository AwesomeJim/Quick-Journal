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
package com.jim.quickjournal.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.jim.quickjournal.model.Journal;

import java.util.Date;

/**
 * Models a journal entry and its properties
 */
@Entity(tableName = "journals")
public class JournalEntry implements Journal{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String body;
    @ColumnInfo(name = "updated_on")
    private Date updatedOn;


    /**
     * Instantiates a new Journal entry.
     *
     * @param title     the title
     * @param body      the body
     * @param updatedOn the updated on
     */
    @Ignore  //tell Room to ignore this Constructor
    public JournalEntry(String title, String body, Date updatedOn) {
        this.title = title;
        this.body = body;
        this.updatedOn = updatedOn;
    }

    /**
     * Instantiates a new Journal entry.
     *
     * @param id        the id
     * @param title     the title
     * @param body      the body
     * @param updatedOn the updated on
     */
    public JournalEntry(int id, String title, String body, Date updatedOn) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.updatedOn = updatedOn;
    }

    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(String body) {
        this.body = body;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Sets updated on.
     *
     * @param updatedOn the updated on
     */
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
