package com.example.project_yougo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.project_yougo.feed.FeedActivity;
import com.example.project_yougo.login.LoginActivity;
import com.example.project_yougo.model.user.UserModel;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        long timestampmillis = 143434344;
//        Date date = new Date(timestampmillis);
//        String dateString = new SimpleDateFormat("dd-MM-yyyy HH::mm::ss").format(date);

        CharSequence text = "loading app..";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;

                if(UserModel.getInstance().isLoggedIn()) {
                    intent = new Intent(SplashActivity.this, FeedActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
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
