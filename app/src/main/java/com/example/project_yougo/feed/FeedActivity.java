package com.example.project_yougo.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.project_yougo.R;
public class FeedActivity extends AppCompatActivity {

    private NavHost navHost;
    NavController navController;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.feed_navHost);
        navController = navHost != null ? navHost.getNavController() : null;

        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.feed_menu,menu);
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
                case R.id.profileFragment_menu:
                    navController.navigate(R.id.action_global_profileFragment);
                    break;
                case R.id.postListFragment_menu:
                    navController.navigate(R.id.action_global_postListFragment);
                    break;
                case R.id.item_post_menu:
                    navController.navigate(R.id.action_global_create_postFragment);
                    break;
                case R.id.aboutFragment_menu:
                    navController.navigate(R.id.action_global_aboutFragment);
                    break;
            }
        }else{
            return true;
        }
        return true;
    }



}