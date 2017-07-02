package com.parnekov.sasha.note.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parnekov.sasha.note.R;
import com.parnekov.sasha.note.activity.NotePagerActivity;
import com.parnekov.sasha.note.data.Note;
import com.parnekov.sasha.note.data.NoteLab;

import java.util.UUID;

/**
 * NoteFragment
 */

public class NoteFragment extends Fragment {
    private static final String ARG_NOTE_ID = "note_id";

    private Note mNote;
    EditText mEditTitle;
    EditText mEditContent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID noteID = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = NoteLab.getNoteLab(getActivity()).getNote(noteID);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        NoteLab.getNoteLab(getActivity()).updateNote(mNote);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note, container, false);
        mEditTitle = (EditText) v.findViewById(R.id.note_title);
        mEditContent = (EditText) v.findViewById(R.id.note_details);
        mEditTitle.setText(mNote.getTitle());
        if (mNote.getTitle() == null) {
            mNote.setTitle("Нова замітка");
        }
        mEditContent.setText(mNote.getContent());


        mEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEditContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return v;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_item, menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save_note:
                this.getActivity().finish();
                return true;
            case R.id.menu_item_delete_note:
                removeNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static NoteFragment newInstance(UUID noteID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteID);

        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(args);
        return noteFragment;
    }

    public void removeNote() {
        UUID noteId = (UUID) getArguments().getSerializable(ARG_NOTE_ID); // створення унікального ідентифікатора
        NoteLab.getNoteLab(getActivity()).removeNote(noteId); // видалення обєкту з таблиці Краймс
        this.getActivity().finish(); // завершує актівіті. повертається на попередню актівіті зякої була викликана.
    }
}
