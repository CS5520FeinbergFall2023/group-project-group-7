package com.NEU23FallGroup7.jobtrackpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar menuToolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuToolbar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);

        // tool bar
        setSupportActionBar(menuToolbar);

        // to do: if not login profile part would be invisible and "login" shows

        // drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,menuToolbar,R.string.Drawer_opening, R.string.Drawer_closing );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //first frag will changed to job search page soon
        navigationView.setCheckedItem(R.id.Resumes);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        int itemId = menuItem.getItemId();
        if (itemId == R.id.Resumes) {
            ResumeManagementFragment resumeManagementFragment = new ResumeManagementFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, resumeManagementFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (itemId == R.id.Applications) {

        }
        return true;
    }
}