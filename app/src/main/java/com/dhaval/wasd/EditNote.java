package com.dhaval.wasd;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditNote extends AppCompatActivity
{
    EditText editNote, editTitle;
    ImageButton closeImageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        editNote = findViewById(R.id.edit_note);
        editTitle = findViewById(R.id.edit_title);
        closeImageButton = findViewById(R.id.closeImageButton);

        editNote.setText(getIntent().getStringExtra("edit_note"));
        editNote.setTransitionName("noteAnime");
        editNote.setTransitionName("noteCardAnime");

        editTitle.setText(getIntent().getStringExtra("edit_title"));

        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}