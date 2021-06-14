package com.example.projectsnakereloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient signInClient;

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());

    }

    public void buttonPressed(View view) {
        switch (view.getId()) {
            case R.id.buttonPlay:
                Intent playIntent = new Intent(this, GameActivity.class);
                startActivity(playIntent);
                break;
            case R.id.buttonGoogle:
                startActivityForResult(signInClient.getSignInIntent(), RC_SIGN_IN);
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


}