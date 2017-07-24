package com.parnekov.sasha.note.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.parnekov.sasha.note.NoteAdapter;
import com.parnekov.sasha.note.R;
import com.parnekov.sasha.note.activity.NoteActivity;
import com.parnekov.sasha.note.data.Note;
import com.parnekov.sasha.note.data.NoteLab;

import java.util.List;

public class NoteListFragment extends Fragment implements NoteAdapter.OnNoteClickListener {

    public static final int REQUEST_CODE = 111;
    private NoteAdapter mNoteAdapter;
    private LinearLayout mEmptyView;
    private List<Note> mNotes;

    @Override
    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mEmptyView = (LinearLayout) view.findViewById(R.id.empty_view);
        RecyclerView noteRecyclerView = (RecyclerView) view.findViewById(R.id.note_recycler_view);
        noteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        NoteLab noteLab = NoteLab.getNoteLab(getActivity());
        mNotes = noteLab.getNotes();
        mNoteAdapter = new NoteAdapter(getActivity(), mNotes, this);
        noteRecyclerView.setAdapter(mNoteAdapter);

        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        checkList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mNotes = NoteLab.getNoteLab(getActivity()).getNotes();
            mNoteAdapter.updateNotes(mNotes);
            checkList();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_note_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_close_app:
                this.getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onNoteClicked(Note note) {
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra(NoteActivity.NOTE_EXTRA, note);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void checkList() {
        if (mNotes.size() > 0) {
            mEmptyView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }
}