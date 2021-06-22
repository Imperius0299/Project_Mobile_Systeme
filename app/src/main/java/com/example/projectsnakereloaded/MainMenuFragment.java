package com.example.projectsnakereloaded;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainMenuFragment extends Fragment implements View.OnClickListener {


    interface Listener {
        void onShowAchievementsRequested();

        void onShowLeaderboardsRequested();

        void onPlayGamesButtonClicked();

        void onPlayButtonClicked();

        void onSettingsButtonClicked();

        // TODO: Add Stats Button
    }

    private Listener listener = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mainmenu, container, false);

        final int[] buttonIds = new int[]{
                R.id.playGames_Button,
                R.id.buttonLeaderboard,
                R.id.settings_Button,
                R.id.buttonAchievements,
                R.id.buttonPlayerStats,
                R.id.buttonPlay
        };

        for (int button : buttonIds) {
            view.findViewById(button).setOnClickListener(this);
        }

        return view;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playGames_Button:
                listener.onPlayGamesButtonClicked();
                break;
            case R.id.buttonLeaderboard:
                listener.onShowLeaderboardsRequested();
                break;
            case R.id.settings_Button:
                listener.onSettingsButtonClicked();
                break;
            case R.id.buttonAchievements:
                listener.onShowAchievementsRequested();
                break;
            case R.id.buttonPlay:
                listener.onPlayButtonClicked();
                break;
        }
    }

}
