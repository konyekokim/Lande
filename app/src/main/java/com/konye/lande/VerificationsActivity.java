package com.konye.lande;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VerificationsActivity extends AppCompatActivity {

    ArrayList<AppVerClass> appVerClassArrayList;
    DrawerLayout mDrawerLayout;
    RelativeLayout historyLayout, settingsLayout, verificationLayout, applicationLayout, navDrawerLayout;
    ImageView topMenuIcon;
    //remember to add the arraylist example
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifications);
        changeWidgetsFont();
        appVerClasses();
        populateSideMenuWidgets();

        ListView listView = (ListView) findViewById(R.id.applications_listView);
        VerificationsAdapter verificationsAdapter = new VerificationsAdapter(this,R.layout.verification_list_view_style,appVerClassArrayList);
        listView.setAdapter(verificationsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //do what you want here
            }
        });

    }

    private ArrayList<AppVerClass> appVerClasses(){
        appVerClassArrayList = new ArrayList<>();
        appVerClassArrayList.add(new AppVerClass("Ikeja District", R.drawable.completed_icon));
        appVerClassArrayList.add(new AppVerClass("Yaba District", R.drawable.pending_icon));
        appVerClassArrayList.add(new AppVerClass("Iju-Ishaga District", R.drawable.completed_icon));
        appVerClassArrayList.add(new AppVerClass("Lekki District", R.drawable.pending_icon));
        appVerClassArrayList.add(new AppVerClass("Ajah District", R.drawable.completed_icon));
        appVerClassArrayList.add(new AppVerClass("Yaba District", R.drawable.completed_icon));
        appVerClassArrayList.add(new AppVerClass("Ikeja District", R.drawable.pending_icon));
        appVerClassArrayList.add(new AppVerClass("Yaba District", R.drawable.pending_icon));

        return appVerClassArrayList;
    }

    private void populateSideMenuWidgets(){
        mDrawerLayout =(DrawerLayout) findViewById(R.id.maps_drawer_layout);
        navDrawerLayout = (RelativeLayout) findViewById(R.id.navigation_drawer_layout);
        historyLayout = (RelativeLayout) findViewById(R.id.history_side_layout);
        verificationLayout = (RelativeLayout) findViewById(R.id.cofo_side_layout);
        applicationLayout = (RelativeLayout) findViewById(R.id.freshlandapp_side_layout);
        settingsLayout = (RelativeLayout) findViewById(R.id.settings_side_layout);
        topMenuIcon = (ImageView) findViewById(R.id.top_menu_icon);
        topMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(navDrawerLayout);
            }
        });
        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        verificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VerificationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        applicationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ApplicationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void changeWidgetsFont(){
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("helvetica_bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
