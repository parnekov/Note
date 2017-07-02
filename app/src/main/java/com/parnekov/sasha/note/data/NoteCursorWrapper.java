package com.parnekov.sasha.note.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.parnekov.sasha.note.data.NoteDBHelper.*;

/**
 * CursorWrapper
 */

public class NoteCursorWrapper extends CursorWrapper {
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(NoteDBTable.UUID));
        String title = getString(getColumnIndex(NoteDBTable.TITLE));
        String content = getString(getColumnIndex(NoteDBTable.CONTENT));
        String dateStr = (getString(getColumnIndex(NoteDBTable.DATE)));

        Note note = new Note(UUID.fromString(uuidString));
        note.setTitle(title);
//        if (title == null) {
//            note.setTitle("Нова замітка");
//        }

        note.setContent(content);
        note.setDate(dateStr);
        if(dateStr == null) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            String dateToStr = format.format(date);
            note.setDate(dateToStr);
        }

        return note;
    }
}
