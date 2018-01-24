package com.parnekov.sasha.note.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parnekov.sasha.note.Note;
import com.parnekov.sasha.note.NoteAdapter;
import com.parnekov.sasha.note.R;
import com.parnekov.sasha.note.activity.NoteActivity;
import com.parnekov.sasha.note.activity.NoteListActivity;
import com.parnekov.sasha.note.data.NoteLab;
import com.parnekov.sasha.note.util.IntentUtils;
import com.parnekov.sasha.note.util.MenuUtils;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteListFragment extends Fragment implements NoteAdapter.OnNoteClickListener, OnMenuItemClickListener {
    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    @BindView(R.id.note_recycler_view)
    RecyclerView noteRecyclerView;

    @OnClick(R.id.fab)
    public void addNew(){
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public static final int REQUEST_CODE = 111;
    private ContextMenuDialogFragment mContextMenu;
    private Context mContext;
    private NoteAdapter mNoteAdapter;
    private List<Note> mNotes;

    @Override
    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setHasOptionsMenu(true);

        mContext = getActivity();
        mContextMenu = MenuUtils.setupMenu(mContext);
        mContextMenu.setItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.fragment_note_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_all:
                if (NoteLab.getNoteLab(getActivity()).getNotes().size() == 0) {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.dialog_delete_no), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    deleteAllItems(getActivity(), mNoteAdapter, mEmptyView);
                }
                checkList();
                return true;
            case R.id.menu_more:
                mContextMenu.show(getFragmentManager(), NoteListActivity.class.getSimpleName());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        noteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper touchHelper = new ItemTouchHelper(new OnSwipeCallback());
        touchHelper.attachToRecyclerView(noteRecyclerView);

        NoteLab noteLab = NoteLab.getNoteLab(getActivity());
        mNotes = noteLab.getNotes();
        mNoteAdapter = new NoteAdapter(getActivity(), mNotes, this);
        noteRecyclerView.setAdapter(mNoteAdapter);

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
    public void onNoteClicked(Note note) {
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra(NoteActivity.NOTE_EXTRA, note);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void checkList() {
        if (mNoteAdapter.getItemCount() > 0) {
            mEmptyView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    IntentUtils.infoIntent(getActivity());
                } else {
                    IntentUtils.infoIntentSDK16(getActivity());
                }
                break;
            case 2:
                IntentUtils.shareIntent(getActivity());
                break;
            case 3:
                IntentUtils.emailIntent(getActivity());
                break;
            case 4:
                IntentUtils.feedbackIntent(getActivity());
                break;
        }
    }

    public static void deleteAllItems(final Context context, final NoteAdapter adapter, final LinearLayout mEmptyView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.dialog_delete_title));
        builder.setPositiveButton((context.getString(R.string.dialog_say_yes)), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NoteLab.getNoteLab(context).removeAllNotes();
                adapter.updateNotes(new ArrayList<Note>());
                dialog.dismiss();
                if (adapter.getItemCount() != 0) {
                    mEmptyView.setVisibility(View.INVISIBLE);
                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }
        });
        builder.setNegativeButton(context.getString(R.string.dialog_say_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public class OnSwipeCallback extends ItemTouchHelper.SimpleCallback {

        OnSwipeCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int position) {

            // Use getTag (from the adapter) to get the id of the swiped ite

            NoteLab.getNoteLab(mContext).removeNote(mNotes.get(position).getId());
            mNoteAdapter.updateNotes(new ArrayList<Note>());
        }
    }
}