package com.example.lupl;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import com.example.lupl.SQL.SQLiteDatabaseHelper;
import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity {
    Toolbar myToolbar;
    TextView username;
    TextView mailAdress;
    SQLiteDatabaseHelper databaseHelper;
    NavigationView navigationView;
    private DrawerLayout drawer;

   // SQLiteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);



        String usernameFromIntent = getIntent().getStringExtra("USERNAME");

        navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.navHeaderUsername);
        username.setText(usernameFromIntent);


        View header2 = navigationView.getHeaderView(0);
        databaseHelper = new SQLiteDatabaseHelper(HomePage.this);
        String email = databaseHelper.getEmail(usernameFromIntent);
        mailAdress = header2.findViewById(R.id.navHeaderEmail);
        mailAdress.setText(email);




        myToolbar = findViewById(R.id.toolbar);

        myToolbar.setTitle(usernameFromIntent);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setBackgroundColor(Color.parseColor("#792E9E"));

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(this,drawer,myToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
}