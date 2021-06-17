package com.example.projectsnakereloaded;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient signInClient;

    private AchievementsClient achievementsClient;
    private LeaderboardsClient leaderboardsClient;
    private PlayersClient playersClient;

    private static final int RC_SIGN_IN = 9001;

    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton = findViewById(R.id.buttonGoogleReal);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        signInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

    }

    public void buttonPressed(View view) {
        switch (view.getId()) {
            case R.id.buttonPlay:
                Intent playIntent = new Intent(this, GameActivity.class);
                startActivity(playIntent);
                break;
            case R.id.buttonGoogleIn:
                startActivityForResult(signInClient.getSignInIntent(), RC_SIGN_IN);
                break;
            case R.id.buttonGoogleOut:
                signOut();
                break;
        }
    }

    public void click(View view) {
        System.out.println("test123");
    }

    //public void signIn (View view)

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
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
                        String displayName;
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

    }

    private void onDisconnected() {
        Log.d("Snake", "onDisconnected()");

        achievementsClient = null;
        leaderboardsClient = null;
        playersClient = null;
    }
}