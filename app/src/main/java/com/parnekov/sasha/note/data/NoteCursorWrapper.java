package com.parnekov.sasha.note.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.parnekov.sasha.note.data.NoteDBHelper.NoteDBTable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteCursorWrapper extends CursorWrapper {

    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(NoteDBTable.UUID));
        String title = getString(getColumnIndex(NoteDBTable.TITLE));
        String content = getString(getColumnIndex(NoteDBTable.CONTENT));
        String dateStr = (getString(getColumnIndex(NoteDBTable.DATE)));

        Note note = new Note(uuidString);
        note.setTitle(title);
        note.setContent(content);
        note.setDate(dateStr);
        if (dateStr == null) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String dateToStr = format.format(date);
            note.setDate(dateToStr);
        }

        return note;
    }
}