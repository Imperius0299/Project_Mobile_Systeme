package com.example.projectsnakereloaded;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import processing.android.PFragment;

public class MainActivity extends AppCompatActivity implements
        Sketch.Callback,
        MainMenuFragment.Listener,
        SettingsFragment.Callback{

    private GoogleSignInClient signInClient;

    private ImageButton buttonLeaderboard;

    private AchievementsClient achievementsClient;
    private LeaderboardsClient leaderboardsClient;
    private PlayersClient playersClient;

    private String displayName;

    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    private static final String TAG = "Snake";

    //private ImageButton playGames_Button;

    private final AccomplishmentsOutbox outbox = new AccomplishmentsOutbox();

    private MainMenuFragment mainMenuFragment;
    private PFragment pFragment;
    private SettingsFragment settingsFragment;

    private Sketch sketch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        signInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

        mainMenuFragment = new MainMenuFragment();
        pFragment = new PFragment();
        settingsFragment = new SettingsFragment();

        mainMenuFragment.setListener(this);
        settingsFragment.setCallback(this);

        getSupportFragmentManager().beginTransaction().add(R.id.container,
                mainMenuFragment).commit();

    }


    // TODO: Prüfen ob erweitern für Settingfragment backpressed / Toolbar hinzufügen?
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                fragment).commit();
    }

    @Override
    public void onPlayButtonClicked() {
        sketch = new Sketch();
        sketch.setCallback(this);
        pFragment.setSketch(sketch);
        switchToFragment(pFragment);
    }

    @Override
    public void onSettingsButtonClicked() {
        switchToFragment(settingsFragment);
    }

    @Override
    public void onPlayGamesButtonClicked() {
        if(displayName == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View promptView = layoutInflater.inflate(R.layout.prompt, null);
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
            alertdialog.setView(promptView);
            alertdialog.setCancelable(false)
                    .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(signInClient.getSignInIntent(), RC_SIGN_IN);
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    });
            alertdialog.create();
            TextView googleloginstatus = (TextView) promptView.findViewById(R.id.googleloginstatus);
            googleloginstatus.setText("You are not signed in.");
            alertdialog.show();
        }
        else {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View promptView = layoutInflater.inflate(R.layout.prompt, null);
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
            alertdialog.setView(promptView);
            alertdialog.setCancelable(false)
                    .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            signOut();
                            displayName = null;
                            Toast.makeText(MainActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    });
            alertdialog.create();
            TextView googleloginstatus = (TextView) promptView.findViewById(R.id.googleloginstatus);
            googleloginstatus.setText("You are signed in as " + displayName + ".");
            alertdialog.show();
        }
    }

    public void buttonPressed(View view) {
        switch (view.getId()) {
            case R.id.buttonPlay:
                Intent playIntent = new Intent(this, GameActivity.class);
                startActivity(playIntent);
                break;
            /*
            case R.id.buttonGoogleIn:
                startActivityForResult(signInClient.getSignInIntent(), RC_SIGN_IN);
                break;
            case R.id.buttonGoogleOut:
                signOut();
                break;
             */
            case R.id.buttonLeaderboard:
                System.out.println("Test12334");
                onShowLeaderboardsRequested();
                break;
        }
    }
/*
    public void getLeaderboardClient() {
        System.out.println(leaderboardsClient);
    }

    public void click(View view) {
        System.out.println("test123");
    }

    public void signIn (View view)
*/
    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    private void signInSilently() {
        Log.d(TAG, "signedInSilently()");

        signInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            onConnected(task.getResult());
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                            onDisconnected();
                        }
                    }
                });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

        signInSilently();
    }

    private void signOut() {

        if (!isSignedIn()) {
            return;
        }

        signInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        Log.d("TAG","signOut(): " + (successful ? "success" : "failed"));
                    }
                });
    }

    public void onShowAchievementsRequested() {
        achievementsClient.getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_UNUSED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                });
    }

    public void onShowLeaderboardsRequested() {
        leaderboardsClient.getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_UNUSED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                });
    }

    public void pushAccomplishments() {
        if (!isSignedIn()) {
            return;
        }

        if (outbox.easyModeScore >= 0) {
            leaderboardsClient.submitScore(getString(R.string.leaderboard_easy_id),
            outbox.easyModeScore);
            outbox.easyModeScore = -1;
        }
    }


    private void updateLeaderboard(int finalScore) {
        if (outbox.easyModeScore < finalScore) {
            outbox.easyModeScore = finalScore;
        }
    }

    private void checkForAchievements(int score) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);

            } catch (ApiException apiException) {
                String msg = apiException.getMessage();
                if (msg != null ||msg.isEmpty()) {
                    msg = getString(R.string.signInError);
                }

                onDisconnected();

                new AlertDialog.Builder(this)
                        .setMessage(msg)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }

    }
    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d("SNAKE", "onConnected(): connected to Google APIs");

        achievementsClient = Games.getAchievementsClient(this, googleSignInAccount);
        leaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);
        playersClient = Games.getPlayersClient(this, googleSignInAccount);

        playersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<Player>() {
                    @Override
                    public void onComplete(Task<Player> task) {
                        if (task.isSuccessful()) {
                            displayName = task.getResult().getDisplayName();
                            String welcomeText = "Welcome:" + displayName;
                            Toast.makeText(getApplicationContext(), welcomeText, Toast.LENGTH_LONG).show();
                        } else {
                            Exception e = task.getException();
                            displayName = "???";
                        }
                    }
                });

        if (!outbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(this, "Your progress will be uploades", Toast.LENGTH_LONG).show();
        }

    }

    private void onDisconnected() {
        Log.d("Snake", "onDisconnected()");

        achievementsClient = null;
        leaderboardsClient = null;
        playersClient = null;
    }

    @Override
    public void onEndedGameScore(int score) {

        checkForAchievements(score);

        updateLeaderboard(score);

        pushAccomplishments();
    }

    private class AccomplishmentsOutbox {
        int easyModeScore = -1;
        int hardModeScore = -1;

        boolean isEmpty() {
            return easyModeScore < 0 && hardModeScore < 0;
        }
    }
}