package com.parnekov.sasha.note.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Note implements Parcelable {

    private String mId;
    private String mTitle;
    private String mContent;
    private String mDate;

    public Note() {
        this(UUID.randomUUID().toString());
    }

    public Note(String id) {
        mId = id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
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

    protected Note(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mContent = in.readString();
        mDate = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mContent);
        parcel.writeString(mDate);
    }
}