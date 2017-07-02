package com.parnekov.sasha.note.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.parnekov.sasha.note.R;
import com.parnekov.sasha.note.activity.NotePagerActivity;
import com.parnekov.sasha.note.data.Note;
import com.parnekov.sasha.note.data.NoteLab;

import java.util.List;

/**
 * Fragment of list
 */

public class NoteListFragment extends Fragment {

    private static final int REQUEST_NOTE = 1;

    private RecyclerView mNoteRecyclerView;
    private NoteAdapter mNoteAdapter;
    TextView mTextFirstView;
    ImageView mImageFirstView;
    FloatingActionButton mActionButton;
    Note mNote;


    @Override
    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        mTextFirstView = (TextView) view.findViewById(R.id.empty_title_text);
        mImageFirstView = (ImageView) view.findViewById(R.id.empty_note_image);
        mImageFirstView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNote();
            }
        });
        mImageFirstView.setVisibility(View.VISIBLE);
        mTextFirstView.setVisibility(View.VISIBLE);
        mNoteRecyclerView = (RecyclerView) view.findViewById(R.id.note_recycler_view);
        mNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newNote();
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View getView() {
        return super.getView();
    }

    public void newNote(){
        mNote = new Note();
        NoteLab.getNoteLab(getActivity()).addNote(mNote);
        Intent intent = NotePagerActivity.newIntent(getActivity(), mNote.getId());
        startActivity(intent);
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {
        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_note, parent, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteHolder holder, int position) {
            Note note = mNotes.get(position);
            holder.bindNote(note);
            if (note != null) {
                mImageFirstView.setVisibility(View.INVISIBLE);
                mTextFirstView.setVisibility(View.INVISIBLE);
            }


        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }

        public void setNotes(List<Note> notes) {
            mNotes = notes;
        }
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Note mNote;

        public NoteHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_note_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_note_title_date);
            itemView.setOnClickListener(this);
        }

        public void bindNote(Note note) {
            mNote = note;
            mTitleTextView.setText(note.getTitle());
            mDateTextView.setText(note.getDate());
        }

        @Override
        public void onClick(View v) {
            Intent intent = NotePagerActivity.newIntent(getActivity(), mNote.getId());
            startActivityForResult(intent, REQUEST_NOTE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_note_list, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_close_app:
                this.getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private void updateUI() {
        NoteLab noteLab = NoteLab.getNoteLab(getActivity());
        List<Note> notes = noteLab.getNotes();


        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(notes);
            mNoteRecyclerView.setAdapter(mNoteAdapter);
        } else {
            mNoteAdapter.setNotes(notes);
            mNoteAdapter.notifyDataSetChanged();
        }
    }

}
