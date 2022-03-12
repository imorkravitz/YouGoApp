package com.example.project_yougo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.project_yougo.feed.FeedActivity;
import com.example.project_yougo.login.LoginActivity;
import com.example.project_yougo.model.AuthenticationHandler;

public class SplashActivity extends AppCompatActivity {
    private AuthenticationHandler authenticationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        authenticationHandler = AuthenticationHandler.getInstance();

        CharSequence text = "loading app..";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;

                if(authenticationHandler.isLoggedIn()) {
                    //intent = new Intent(SplashActivity.this, LoginActivity.class);
                    toFeedActivity();
                } else {
                    //intent = new Intent(SplashActivity.this, LoginActivity.class);
                    toLoginActivity();
                }
//                startActivity(intent);
//                finish();
            }
        }, 3000);

    }


    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toFeedActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
