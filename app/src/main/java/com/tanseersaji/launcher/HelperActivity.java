/*
* Copyright (C) 2017 Tanseer Saji - https://goo.gl/5F9Dgy
*
*
* Licensed under the GNU Affero General Public License v3.0
*
* Everyone is permitted to copy and distribute verbatim copies
* of this license document, but changing it is not allowed.
*
* Permissions of this strongest copyleft license are
* conditioned on making available complete source code
* of licensed works and modifications, which include larger works
* using a licensed work, under the same license. Copyright and
* license notices must be preserved. Contributors provide an express
* grant of patent rights. When a modified version is used to provide
* a service over a network, the complete source code of the modified
* version must be made available.
*
*
* MORE INFORMATION AT https://github.com/TheInvertedTriangle/No-Launcher/blob/master/LICENSE
 */

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

        showAppBar.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("showAppBar",showAppBar.isChecked());
            editor.apply();
        });
        showClock.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("showClock",showClock.isChecked());
            editor.apply();
        });


        settingButton = (Button) findViewById(R.id.openPhoneSettings);
        settingButton.setOnClickListener(view -> startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0));

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
