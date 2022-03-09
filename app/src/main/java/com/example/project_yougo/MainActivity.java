package com.example.project_yougo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    NavHost navHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.base_navHost);
        navController = navHost != null ? navHost.getNavController() : null;

        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!super.onOptionsItemSelected(item))
        {
            switch (item.getItemId()){
                case android.R.id.home:
                    navController.navigateUp();
                    break;
                case R.id.aboutFragment_menu:
                    navController.navigate(R.id.action_global_aboutFragment);
                    break;
                case R.id.profileFragment_menu:
                    navController.navigate(R.id.action_global_profileFragment);
                    break;
                case R.id.postListFragment_menu:
                    navController.navigate(R.id.action_global_postListFragment);
            }
        }else{
            return true;
        }
        return true;
    }

    public void onRegisterButtonClick(View view) {
        RegisterFragment registerFragment = (RegisterFragment)(getSupportFragmentManager()
                .findFragmentById(R.id.registerFragment));
        registerFragment.register(view);
    }
}
