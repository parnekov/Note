package com.parnekov.sasha.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.parnekov.sasha.note.R;
import com.parnekov.sasha.note.data.Note;
import com.parnekov.sasha.note.fragment.NoteFragment;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_EXTRA = "note_extra";

    public interface OnBackPressedListener {
        void onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intent = getIntent();
        Note note = null;
        if (intent.hasExtra(NOTE_EXTRA)) {
            note = getIntent().getParcelableExtra(NOTE_EXTRA);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.note_container);

        if (fragment == null) {
            fragment = NoteFragment.newInstance(note);
            fragmentManager.beginTransaction().add(R.id.note_container, fragment).commit();
        }
    }



    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

}