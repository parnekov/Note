package com.parnekov.sasha.note.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class NoteDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "NoteBase.db";

    NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NoteDBTable.NAME + " (" +
                NoteDBTable._ID + " INTEGER PRIMARY KEY," +
                NoteDBTable.UUID + " INTEGER NOT NULL," +
                NoteDBTable.TITLE + " TEXT," +
                NoteDBTable.DATE + " TEXT," +
                NoteDBTable.CONTENT + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteDBTable.NAME);
        onCreate(db);
    }

    class NoteDBTable implements BaseColumns {
        static final String NAME = "notes";
        static final String UUID = "uuid";
        static final String TITLE = "title";
        static final String CONTENT = "content";
        static final String DATE = "date";
    }
}