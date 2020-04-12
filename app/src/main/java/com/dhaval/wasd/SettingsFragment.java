package com.dhaval.wasd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat
{
    private static final String THEME_KEY = "themeList";
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.settings, rootKey);

        ListPreference listPreference = findPreference(THEME_KEY);

        assert listPreference != null;
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.equals("Light"))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                else if (newValue.equals("Dark"))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else if(newValue.equals("Auto"))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

                Objects.requireNonNull(getActivity()).recreate();
                return true;
                //startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }
}
