package com.example.projectsnakereloaded;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    interface Callback {
        //TODO: Anschauen ob Callback sinnvoll oder Stack machen
    }

    Callback callback = null;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        /*
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

         */
    }


}