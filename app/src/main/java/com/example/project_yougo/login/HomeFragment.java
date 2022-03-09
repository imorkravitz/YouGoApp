package com.example.project_yougo.login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.project_yougo.R;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    ViewPager viewPager;
    Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_home,container, false);
        login = (Button) view.findViewById(R.id.login_login_btn);

        viewPager = view.findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.add(new RegisterFragment(), "Register");
        viewPagerAdapter.add(new LoginFragment(), "Login");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout =  view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
