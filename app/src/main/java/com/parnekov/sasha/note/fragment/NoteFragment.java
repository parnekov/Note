package com.parnekov.sasha.note.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parnekov.sasha.note.R;
import com.parnekov.sasha.note.data.Note;
import com.parnekov.sasha.note.data.NoteLab;

public class NoteFragment extends Fragment {

    private static final String ARG_NOTE = "arg_note";
    private Note mNote;
    private EditText mEditTitle;
    private EditText mEditContent;

    public static NoteFragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(args);
        return noteFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNote = getArguments().getParcelable(ARG_NOTE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mEditTitle = (EditText) view.findViewById(R.id.note_title);
        mEditContent = (EditText) view.findViewById(R.id.note_details);
        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrUpdateNote();
            }
        });

        if (mNote != null) {
            mEditTitle.setText(mNote.getTitle());
            mEditContent.setText(mNote.getContent());
        }

        mEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNote != null) mNote.setTitle(s.toString());
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
                if (mNote != null) mNote.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_note:
                removeNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addOrUpdateNote() {
        if (mNote == null) {
            mNote = new Note();
            mNote.setTitle(mEditTitle.getText().toString());
            mNote.setContent(mEditContent.getText().toString());
            NoteLab.getNoteLab(getActivity()).addNote(mNote);
        } else {
            NoteLab.getNoteLab(getActivity()).updateNote(mNote);
        }
        finishActivity();
    }

    private void removeNote() {
        if (mNote != null) NoteLab.getNoteLab(getActivity()).removeNote(mNote.getId());
        finishActivity();
    }

    private void finishActivity() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}