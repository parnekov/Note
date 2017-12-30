package com.parnekov.sasha.note.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parnekov.sasha.note.Note;
import com.parnekov.sasha.note.data.NoteDBHelper.NoteDBTable;

import java.util.ArrayList;
import java.util.List;

public class NoteLab {

    private static NoteLab sNoteLab;
    private SQLiteDatabase mDatabase;

    public static NoteLab getNoteLab(Context context) {
        if (sNoteLab == null) {
            sNoteLab = new NoteLab(context);
        }
        return sNoteLab;
    }

    private NoteLab(Context context) {
        Context cont = context.getApplicationContext();
        mDatabase = new NoteDBHelper(cont).getWritableDatabase();

    }

    public void addNote(Note note) {
        ContentValues values = getContentValues(note);
        mDatabase.insert(NoteDBTable.NAME, null, values);
    }


    public void removeNote(String id) {
        mDatabase.delete(NoteDBTable.NAME,
                NoteDBTable.UUID + " = ?",
                new String[]{id});
    }

    public void removeAllNotes() {
        if (mDatabase != null) mDatabase.delete(NoteDBTable.NAME, null, null);
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        NoteCursorWrapper cursor = queryNotes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return notes;

    }

    public void updateNote(Note note) {
        String uuidString = note.getId();
        ContentValues values = getContentValues(note);
        mDatabase.update(NoteDBTable.NAME, values, NoteDBTable.UUID + " = ?",
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteDBTable.UUID, note.getId());
        values.put(NoteDBTable.TITLE, note.getTitle());
        values.put(NoteDBTable.CONTENT, note.getContent());
        values.put(NoteDBTable.DATE, note.getDate());
        return values;
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteDBTable.NAME,
                null, // Columns - null - choose all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new NoteCursorWrapper(cursor);
    }
}