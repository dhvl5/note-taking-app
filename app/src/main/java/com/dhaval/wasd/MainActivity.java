package com.dhaval.wasd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

enum FloatingButtonState
{
    ADD_STATE, SAVE_STATE
};

public class MainActivity extends AppCompatActivity
{
    FloatingButtonState floatingButtonState;

    EditText editText;
    FloatingActionButton floatingActionButton;

    private RelativeLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;

        recyclerView = findViewById(R.id.rv);
        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, noteList);

        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        editText = findViewById(R.id.note_edt_txt);
        floatingActionButton = findViewById(R.id.floating_btn);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);

        floatingButtonState = FloatingButtonState.ADD_STATE;
        floatingActionButton.setVisibility(View.VISIBLE);

        if(floatingButtonState == FloatingButtonState.ADD_STATE)
            floatingActionButton.setImageResource(R.drawable.ic_add_black);
        else if(floatingButtonState == FloatingButtonState.SAVE_STATE)
            floatingActionButton.setImageResource(R.drawable.ic_save_black);

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(floatingButtonState == FloatingButtonState.ADD_STATE)
                {

                }
                Note a = new Note(editText.getText().toString());
                noteList.add(a);
                noteAdapter.notifyDataSetChanged();

                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.clearFocus();
                editText.getText().clear();
            }
        });

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View view, View v)
            {
                Intent intent = new Intent(getApplicationContext(), EditNote.class);
                intent.putExtra("edit_note", noteList.get(position).getNote());
                /*intent.putExtra("noteAnime", ViewCompat.getTransitionName(view));
                intent.putExtra("noteCardAnime", ViewCompat.getTransitionName(v));*/
                Pair<View, String> pair1 = new Pair<>(view, ViewCompat.getTransitionName(view));
                Pair<View, String> pair2 = new Pair<>(v, ViewCompat.getTransitionName(v));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, pair1, pair2);
                startActivity(intent, options.toBundle());
            }
        });

        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() > 0)
                    floatingActionButton.setVisibility(View.VISIBLE);
                else
                    floatingActionButton.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        if(inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0))
            editText.clearFocus();

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    editText.clearFocus();
                }
            }
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback()
        {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {

            }
        });
    }
}
