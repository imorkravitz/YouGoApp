package com.example.project_yougo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.base_navHost);
        navController = navHost != null ? navHost.getNavController() : null;

    }
}
