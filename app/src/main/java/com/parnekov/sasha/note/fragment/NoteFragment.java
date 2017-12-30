package com.parnekov.sasha.note.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.parnekov.sasha.note.Note;
import com.parnekov.sasha.note.R;
import com.parnekov.sasha.note.activity.NoteActivity;
import com.parnekov.sasha.note.data.NoteLab;
import com.parnekov.sasha.note.util.MenuUtils;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;

public class NoteFragment extends Fragment implements NoteActivity.OnBackPressedListener {

    private static final String ARG_NOTE = "arg_note";
    private Note mNote;
    private EditText mEditTitle;
    private EditText mEditContent;

    private String mTitle = "";
    private String mContent = "";

    private ContextMenuDialogFragment mContextMenu;
    private Context mContext;

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
        mContext = getActivity();
        mContextMenu = MenuUtils.setupMenu(mContext);
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
            mTitle = mNote.getTitle();
            mEditTitle.setText(mTitle);
            mContent = mNote.getContent();
            mEditContent.setText(mContent);
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
                if (mNote == null) {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.dialog_delete_note_no), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    removeOneNoteDialog();
                }
                return true;
            case android.R.id.home:
                checkAndFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addOrUpdateNote() {
        String isEmpty = mEditTitle.getText().toString();
        if (isEmpty.length() == 0) {
            Toast toast = Toast.makeText(getActivity(), mContext.getString(R.string.note_title_fill), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else {
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
    }

    private void removeNote() {
        if (mNote != null) NoteLab.getNoteLab(getActivity()).removeNote(mNote.getId());
        finishActivity();
    }

    private void removeOneNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.dialog_delete_note));
        builder.setPositiveButton((getActivity().getString(R.string.dialog_say_yes)), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeNote();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getActivity().getString(R.string.dialog_say_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void finishActivity() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void checkAndFinish() {
        boolean title = mEditTitle.getText().toString().equals(mTitle);
        boolean content = mEditContent.getText().toString().equals(mContent);
        if (title && content) {
            NavUtils.navigateUpFromSameTask(getActivity());
            getActivity().finish();
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_title));
        builder.setPositiveButton(getString(R.string.dialog_say_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                NavUtils.navigateUpFromSameTask(getActivity());
                getActivity().finish();
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_say_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        checkAndFinish();
    }
}