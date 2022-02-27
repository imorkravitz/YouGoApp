package com.example.project_yougo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    NavController navController;
    ViewPager viewPager;
    ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.base_navHost);
        navController = navHost.getNavController();

        viewPager = findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.add(new RegisterFragment(), "Login");
        viewPagerAdapter.add(new LoginFragment(), "Register");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout =  findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}