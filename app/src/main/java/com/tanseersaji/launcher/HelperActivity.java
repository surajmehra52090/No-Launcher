package com.tanseersaji.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

public class HelperActivity extends Activity {
    ToggleButton showAppBar;
    ToggleButton showClock;
    Button settingButton;
    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);


        MobileAds.initialize(this, "ca-app-pub-6440435975761242~6952366562");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        SharedPreferences sharedPreferences = getSharedPreferences("NoLauncherSettings", Context.MODE_PRIVATE);
        showAppBar = findViewById(R.id.showAppBar);
        showClock = findViewById(R.id.showClock);

        showAppBar.setTextOff("Show");
        showAppBar.setTextOn("Hide");

        showClock.setTextOff("Hide");
        showClock.setTextOn("Show");

        if(!sharedPreferences.contains("showClock")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("showClock",true);
            editor.apply();
        }

        if (isHomeApp()){
            showAppBar.setChecked(sharedPreferences.getBoolean("showAppBar",false));
            showClock.setChecked(sharedPreferences.getBoolean("showClock",false));
        }
        else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                final Intent intent = new Intent(Settings.ACTION_HOME_SETTINGS);
                startActivity(intent);
            }
            else {
                final Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

        showAppBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("showAppBar",showAppBar.isChecked());
                editor.apply();
            }
        });
        showClock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("showClock",showClock.isChecked());
                editor.apply();
            }
        });


        settingButton = (Button) findViewById(R.id.openPhoneSettings);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
            }
        });

    }

    boolean isHomeApp() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo != null && getPackageName()
                .equals(res.activityInfo.packageName)) {
            return true;
        }
        return false;
    }

}
