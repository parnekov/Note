package com.parnekov.sasha.note.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * DB Helper
 */

public class NoteDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "NoteBase.db";

    public NoteDBHelper(Context context) {
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
//        db.execSQL("DROP TABLE IF EXISTS " + NAME);
//        onCreate(db);
    }

    public class NoteDBTable implements BaseColumns {
        public static final String NAME = "notes";
        public static final String UUID = "uuid";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String DATE = "date";
    }
}