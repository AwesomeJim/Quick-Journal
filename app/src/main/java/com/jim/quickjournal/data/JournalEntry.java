package com.jim.quickjournal.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import java.util.Date;


@Entity(tableName = "journals")
public class JournalEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String body;
    @ColumnInfo(name = "updated_on")
    private Date updatedOn;


    @Ignore  //tell Room to ignore this Constructor
    public JournalEntry(String title, String body, Date updatedOn) {
        this.title = title;
        this.body = body;
        this.updatedOn = updatedOn;
    }

    public JournalEntry(int id, String title, String body, Date updatedOn) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.updatedOn = updatedOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
