package com.example.projectsnakereloaded;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Represents the Main Menu of the Game
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {

    /**
     * The Interface for listening to the Button clicks.
     */
    interface Listener {
        void onShowAchievementsRequested();

        void onShowLeaderboardsRequested();

        void onPlayGamesButtonClicked();

        void onPlayButtonClicked();

        void onSettingsButtonClicked();

        void onPlayerstatsButtonClicked();

    }

    private Listener listener = null;

    private ImageButton buttonAchievements;
    private ImageButton buttonLeaderboard;

    
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

        buttonAchievements = view.findViewById(R.id.buttonAchievements);
        buttonLeaderboard = view.findViewById(R.id.buttonLeaderboard);

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
            case R.id.buttonPlayerStats:
                listener.onPlayerstatsButtonClicked();
                break;
            case R.id.buttonPlay:
                listener.onPlayButtonClicked();
                break;
        }
    }

    //Todo : fixen wenn von fragmet zurück
    public void updateButtons(boolean isSignedIn) {
        if (!isSignedIn) {
            buttonAchievements.setEnabled(false);
            buttonLeaderboard.setEnabled(false);

            buttonAchievements.getDrawable().setColorFilter(Color.argb(155, 211,211,211), PorterDuff.Mode.SRC_ATOP);
            buttonLeaderboard.getDrawable().setColorFilter(Color.argb(155, 211,211,211), PorterDuff.Mode.SRC_ATOP);
        } else {
            buttonAchievements.setEnabled(true);
            buttonLeaderboard.setEnabled(true);
            buttonAchievements.getDrawable().setColorFilter(null);
            buttonLeaderboard.getDrawable().setColorFilter(null);
        }
    }

}
