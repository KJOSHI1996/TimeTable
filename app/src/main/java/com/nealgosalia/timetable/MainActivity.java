package com.nealgosalia.timetable;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ncapdevi.fragnav.FragNavController;
import com.nealgosalia.timetable.activities.AttendanceActivity;
import com.nealgosalia.timetable.activities.PreferencesActivity;
import com.nealgosalia.timetable.fragments.SubjectsFragment;
import com.nealgosalia.timetable.fragments.TimetableFragment;
import com.nealgosalia.timetable.fragments.TodayFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;
    private final int TAB_THIRD = FragNavController.TAB3;
    boolean doubleBackToExitPressedOnce = false;
    private FragNavController fragNavController;
    private Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        PrimaryDrawerItem attendance = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.attendance);
        PrimaryDrawerItem settings = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.settings);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .build();
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar)
                .addDrawerItems(
                        attendance,
                        settings
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("MainActivity","Position is "+position);
                        switch(position){
                            case 1:
                                Intent i1=new Intent(getApplication(), AttendanceActivity.class);
                                startActivity(i1);
                                break;
                            case 2:
                                Intent i2=new Intent(getApplication(), PreferencesActivity.class);
                                startActivity(i2);
                                break;
                        }
                        result.deselect();
                        result.closeDrawer();
                        return true;
                    }
                })
                .build();
        result.deselect();
        List<Fragment> fragments = new ArrayList<>(3);
        fragments.add(new TimetableFragment());
        fragments.add(new TodayFragment());
        fragments.add(new SubjectsFragment());
        fragNavController = new FragNavController(savedInstanceState, getSupportFragmentManager(), R.id.contentContainer, fragments, TAB_FIRST);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTabPosition(1);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_timetable:
                        fragNavController.switchTab(TAB_FIRST);
                        break;
                    case R.id.tab_today:
                        fragNavController.switchTab(TAB_SECOND);
                        break;
                    case R.id.tab_subjects:
                        fragNavController.switchTab(TAB_THIRD);
                        break;
                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                fragNavController.clearStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(result != null && result.isDrawerOpen()){
            result.closeDrawer();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back button again to exit!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

}
