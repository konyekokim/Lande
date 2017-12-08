package com.konye.lande;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HistoryActivity extends AppCompatActivity {

    RelativeLayout applicationsLayout, verificationsLayout;
    ImageView openAppImgView, openVerImgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        changeWidgetsFont();

        applicationsLayout = (RelativeLayout) findViewById(R.id.applications_layout);
        verificationsLayout = (RelativeLayout) findViewById(R.id.verifications_layout);
        openAppImgView = (ImageView) findViewById(R.id.open_app_imgView);
        openVerImgView = (ImageView) findViewById(R.id.open_ver_imgView);

        applicationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want here
                Intent intent = new Intent(getApplicationContext(), ApplicationsActivity.class);
                startActivity(intent);
            }
        });
        verificationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want here
            }
        });
        openAppImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want here 
                Intent intent = new Intent(getApplicationContext(), VerificationsActivity.class);
                startActivity(intent);
            }
        });
        openVerImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want here
            }
        });

    }

    private void changeWidgetsFont(){
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("helvetica_font_normal.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
