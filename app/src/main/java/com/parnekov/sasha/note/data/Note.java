package com.parnekov.sasha.note.data;

import java.util.Date;
import java.util.UUID;

/**
 * Model
 */

public class Note {
    private UUID mId;
    private String mTitle;
    private String mContent;
    private String mDate;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public Note() {
        this(UUID.randomUUID());
    }

    public Note(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

}
