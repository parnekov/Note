package com.parnekov.sasha.note.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parnekov.sasha.note.R;
import com.parnekov.sasha.note.fragment.NoteListFragment;
import com.parnekov.sasha.note.fragment.SingleFragmentActivity;

public class NoteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}

