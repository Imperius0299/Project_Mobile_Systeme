package com.example.projectsnakereloaded;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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

/* Menu Sound by
    Superepic by Alexander Nakarada | https://www.serpentsoundstudios.com
    Music promoted by https://www.chosic.com
    Attribution 4.0 International (CC BY 4.0)
    https://creativecommons.org/licenses/by/4.0/
*/
public class MainActivity extends AppCompatActivity implements
        Sketch.Callback,
        MainMenuFragment.Listener{

    private GoogleSignInClient signInClient;

    private AchievementsClient achievementsClient;
    private LeaderboardsClient leaderboardsClient;
    private PlayersClient playersClient;

    private String displayName;

    private Sketch sketch;

    private MediaPlayer mp;

    private AppDatabase database;

    private MainMenuFragment mainMenuFragment;
    private PFragment pFragment;
    private SettingsFragment settingsFragment;

    private  SharedPreferences.OnSharedPreferenceChangeListener listener;

    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    private static final String TAG = "Snake";
    public static final String BACKSTACK_KEY = "myBackstack";

    private final AccomplishmentsOutbox outbox = new AccomplishmentsOutbox();



    //Window Fullscreen https://www.tutorialspoint.com/how-to-get-full-screen-activity-in-android

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        // Lösung für Aspect Ratio / Black Bar https://stackoverflow.com/a/60259591

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        signInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

        mainMenuFragment = new MainMenuFragment();
        pFragment = new PFragment();
        settingsFragment = new SettingsFragment();

        mainMenuFragment.setListener(this);

        database = AppDatabase.getDatabase(this);

        mp = MediaPlayer.create(this, R.raw.loyalty_freak_music_everyone_is_so_alive);
        mp.setLooping(true);

        //Inspiration für Shared preferences listener durch https://stackoverflow.com/a/33509405

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("music")) {
                    checkMusicAndPlay(sharedPreferences);
                }
            }
        };

        getSupportFragmentManager().beginTransaction().add(R.id.container,
                mainMenuFragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }


    //Todo: Problem wenn Musik ausgeschaltet wird fixen

    /**
     * Checks the music setting and plays or pauses music depending on the switch.
     * @param sharedPreferences - An instance of the shared preferences.
     */
    public void checkMusicAndPlay(SharedPreferences sharedPreferences) {
        SharedPreferences prefs = sharedPreferences;
        if (prefs.getBoolean("music", true)) {
            mp.start();
        } else {
            if (!mp.isPlaying()) {
                return;
            }
            mp.pause();
        }
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

    /**
     * Handler for replacing and adding fragments to the backstack.
     * @param fragment - The fragment which should be shown.
     */
    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                fragment).addToBackStack(BACKSTACK_KEY).setReorderingAllowed(true).commit();
    }

    /**
     * Handler for the Google Play Games button. Starts an alert dialog to start sign-in intent or for signing out.
     */
    @Override
    public void onPlayGamesButtonClicked() {
        if(displayName == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View promptView = layoutInflater.inflate(R.layout.prompt, null);
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
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
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
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

    /**
     * Handler for the settings button. Switches to the settings fragment.
     */
    @Override
    public void onSettingsButtonClicked() {
        switchToFragment(settingsFragment);
    }

    /**
     * Handler for the Player Stats button. Starts an alert dialog that shows the local player stats.
     */
    @Override
    public void onPlayerstatsButtonClicked() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View playerstatsView = layoutInflater.inflate(R.layout.playerstats, null);
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        alertdialog.setView(playerstatsView)
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close
                    }
                })
                .create();

        TextView highestScoreTextView = (TextView) playerstatsView.findViewById(R.id.highestScore);
        TextView totalScoreTextView = (TextView) playerstatsView.findViewById(R.id.totalScore);
        TextView totalDeathsTextView = (TextView) playerstatsView.findViewById(R.id.totalDeaths);
        TextView totalFieldsMovedTextView = (TextView) playerstatsView.findViewById(R.id.totalFieldsMoved);
        TextView totalItemsPickedUpTextView = (TextView) playerstatsView.findViewById(R.id.totalItemsPickedUp);

        Stats stats = database.statsDao().getStats();
        String highestScore = "Highest Score: " + stats.highestScore;
        String totalScore = "Total Score: " + stats.totalScore ;
        String totalDeaths = "Total Deaths: "  + stats.totalDeaths;
        String totalFieldsMoved = "Total Fields Moved: " + stats.totalFieldsMoved;
        String totalItemsPickedUp = "Total Items Picked Up: " + stats.totalItemsPickedUp;

        highestScoreTextView.setText(highestScore);
        totalScoreTextView.setText(totalScore);
        totalDeathsTextView.setText(totalDeaths);
        totalFieldsMovedTextView.setText(totalFieldsMoved);
        totalItemsPickedUpTextView.setText(totalItemsPickedUp);

        alertdialog.show();

    }

    /**
     * Handler for the Play Button. Initializes a sketch and switches to the PFragment.
     */
    @Override
    public void onPlayButtonClicked() {

        sketch = new Sketch();
        sketch.setCallback(this);
        pFragment.setSketch(sketch);
        switchToFragment(pFragment);
    }

    /**
     * Handler for the Achievements button. When successful, the achievements intent is started.
     */
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

    /**
     * Handler for the Leaderboards button. When successful, the leaderboards intent is started.
     */

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

    /**
     * Checks if a player was signed in in the last session.
     * @return Boolean if the Player was signed-in.
     */
    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    /**
     * Methode that tries to automatic sign-in without noticing the user.
     */
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

    // Todo: vielleicht noch abändern
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");

        signInSilently();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(listener);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        checkMusicAndPlay(prefs);
       // mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(listener);
        mp.pause();
    }

    /**
     * Signs out the active signed-in account.
     */
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
                        if (successful) {
                            onDisconnected();
                        }
                    }
                });
    }


    /**
     * Pushes the values that are locally saved in the accomplishment outbox to Google Play.
     */
    public void pushAccomplishments() {
        if (!isSignedIn()) {
            return;
        }

        if (outbox.easyModeScore >= 0) {
            leaderboardsClient.submitScore(getString(R.string.leaderboard_easy_id),
            outbox.easyModeScore);
            outbox.easyModeScore = -1;
        }
        if (outbox.firstPoint) {
            achievementsClient.unlock(getString(R.string.first_point));
            outbox.firstPoint = false;
        }
        if (outbox.goodStart) {
            achievementsClient.unlock(getString(R.string.good_start));
            outbox.goodStart = false;
        }
        if (outbox.longerBetter) {
            achievementsClient.unlock(getString(R.string.longer_better));
            outbox.longerBetter = false;
        }
        if (outbox.wasThereAnything) {
            achievementsClient.unlock(getString(R.string.wall_hit));
            outbox.wasThereAnything = false;
        }
        if (outbox.longRun > 0) {
            achievementsClient.increment(getString(R.string.long_run),
                    outbox.longRun);
            outbox.longRun = 0;
        }
        if (outbox.unlimitedPower > 0) {
            achievementsClient.increment(getString(R.string.unlimited_power),
                    outbox.unlimitedPower);
            outbox.unlimitedPower = 0;
        }
    }


    /**
     *
     * @param finalScore
     */
    private void updateLeaderboard(int finalScore) {
        if (outbox.easyModeScore < finalScore) {
            outbox.easyModeScore = finalScore;
        }
    }

    /**
     * Checks if the requirements for achievements were given and updates the outbox.
     * @param score - The final score of the last game.
     * @param wallHit - Boolean if a wall was hit.
     * @param itemsPickedUp - The number of items picked up.
     */
    private void checkForAchievements(int score, boolean wallHit, int itemsPickedUp) {
        if (score >= 1) {
            outbox.firstPoint = true;
        }
        if (score == 25) {
            outbox.longerBetter = true;
        }
        if (wallHit) {
            outbox.wasThereAnything = true;
        }
        outbox.unlimitedPower += itemsPickedUp;
        outbox.longRun++;
        outbox.goodStart = true;
    }

    /**
     * Handler for take in the stats of the last game and calls methods for updating achievements,
     * leaderboard, local stats and for pushing them to the Google Cloud.
     * @param score - The final score of the game.
     * @param itemsPickedUp - The number of items picked up.
     * @param fieldsMoved - Number of fields that were covered.
     * @param wallHit - Boolean if a wall was hit.
     */
    @Override
    public void onEndedGameScore(int score, int itemsPickedUp, int fieldsMoved, boolean wallHit) {

        checkForAchievements(score, wallHit, itemsPickedUp);

        updateLeaderboard(score);

        pushAccomplishments();

        updateLocalStats(score, itemsPickedUp, fieldsMoved);
    }

    //Todo: int itemsPickedUp, int obstaclesDestroyed einfügen

    /**
     * Updates the local stats that are saved in the database.
     * @param score - The final score of the last game.
     * @param itemsPickedUp - The number of items picked up.
     * @param fieldsMoved - Number of fields that were covered.
     */
    public void updateLocalStats(int score, int itemsPickedUp, int fieldsMoved) {
        Stats stats = database.statsDao().getStats();
        if (stats != null) {
            database.statsDao().updateStats(score > stats.highestScore ? score : stats.highestScore,
                    stats.totalScore + score, ++stats.totalDeaths,
                    stats.totalItemsPickedUp + itemsPickedUp,
                    stats.totalFieldsMoved + fieldsMoved,
                    stats.id);
        } else {
            Stats insertedStats = new Stats(score, score, 1, itemsPickedUp, fieldsMoved);
            database.statsDao().addStats(insertedStats);
        }

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

    /**
     * Handler successful for connection. Sets the different API clients (achievements, leaderboard, player) depending on the signed-in account.
     * Also pushes achievements and leaderboards values.
     * @param googleSignInAccount - The signed in account of the player
     */
    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d("SNAKE", "onConnected(): connected to Google APIs");

        achievementsClient = Games.getAchievementsClient(this, googleSignInAccount);
        leaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);
        playersClient = Games.getPlayersClient(this, googleSignInAccount);

        mainMenuFragment.updateButtons(true);

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
            Toast.makeText(this, "Your progress will be uploaded", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Handler for disconnection. Updates the button visibility and the API clients.
     */
    private void onDisconnected() {
        Log.d("Snake", "onDisconnected()");

        achievementsClient = null;
        leaderboardsClient = null;
        playersClient = null;

        mainMenuFragment.updateButtons(false);
    }


    @Override
    protected void onDestroy() {
        mp.release();
        AppDatabase.destroyInstance();
        super.onDestroy();
    }


    /**
     * Represents the accomplishment outbox that saves the gathered achievements and score locally,
     * so that it can be pushed later. It is also for preventing a loss of the accomplishments when
     * the connection is lost during a session.
     */
    private class AccomplishmentsOutbox {
        boolean firstPoint = false;
        boolean goodStart = false;
        boolean longerBetter = false;
        boolean wasThereAnything = false;

        int longRun = 0;
        int unlimitedPower = 0;

        int easyModeScore = -1;
        int hardModeScore = -1;

        /**
         * Checks if the accomplishments outbox is empty, so no accomplishments were gathered during the last push.
         * @return Boolean if box is empty.
         */
        boolean isEmpty() {
            return easyModeScore < 0 && hardModeScore < 0
                    && firstPoint == false && goodStart == false && longerBetter == false
                    && wasThereAnything == false && longRun == 0 && unlimitedPower == 0;
        }
    }
}