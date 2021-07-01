package com.example.projectsnakereloaded;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Fragment that represents the settings of the game.
 * @author Alexander Storbeck
 * @author Luca Jetter
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

    }


}