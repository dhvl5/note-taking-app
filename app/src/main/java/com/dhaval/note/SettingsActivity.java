package com.dhaval.note;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{
    ImageButton backImageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backImageButton = findViewById(R.id.backImageButton);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new SettingsFragment()).commit();

        backImageButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }
}
