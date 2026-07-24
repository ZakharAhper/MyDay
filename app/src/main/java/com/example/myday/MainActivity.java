package com.example.myday;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        openFragment(new TodayFragment());

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_today) {
                openFragment(new TodayFragment());
                return true;
            }

            if (itemId == R.id.nav_history) {
                openFragment(new HistoryFragment());
                return true;
            }

            if (itemId == R.id.nav_stats) {
                openFragment(new StatsFragment());
                return true;
            }
            if (itemId == R.id.nav_calendar) {
                openFragment(new CalendarFragment());
                return true;
            }

            return false;
        });
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.screenContainer, fragment)
                .commit();
    }
}