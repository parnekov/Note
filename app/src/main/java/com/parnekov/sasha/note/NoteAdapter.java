package com.parnekov.sasha.note;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private Context mContext;
    private List<Note> mNotes;
    private OnNoteClickListener mClickListener;

    public NoteAdapter(Context context, List<Note> notes, OnNoteClickListener clickListener) {
        mContext = context;
        mNotes = notes;
        mClickListener = clickListener;
    }

    @Override
    public NoteAdapter.NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_note, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.NoteHolder holder, int position) {
        holder.bindNote(position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void updateNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.list_item_note_title_text_view)
        TextView mTitleTextView;

        @BindView(R.id.list_item_note_title_date)
        TextView mDateTextView;

        NoteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindNote(int position) {
            mTitleTextView.setText(mNotes.get(position).getTitle());
            mDateTextView.setText(mNotes.get(position).getDate());
        }

        @Override
        public void onClick(View view) {
            mClickListener.onNoteClicked(mNotes.get(getAdapterPosition()));
        }
    }

    public interface OnNoteClickListener {
        void onNoteClicked(Note note);
    }
}
