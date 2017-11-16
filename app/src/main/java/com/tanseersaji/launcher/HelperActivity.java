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
import android.widget.Button;

import android.widget.ToggleButton;


public class HelperActivity extends Activity {
    ToggleButton showAppBar;
    ToggleButton showClock;
    ToggleButton revView;
    Button settingButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        SharedPreferences sharedPreferences = getSharedPreferences("NoLauncherSettings", Context.MODE_PRIVATE);
        showAppBar = findViewById(R.id.showAppBar);
        showClock = findViewById(R.id.showClock);
        revView = findViewById(R.id.revView);

        showAppBar.setTextOff("Show");
        showAppBar.setTextOn("Hide");

        showClock.setTextOff("Hide");
        showClock.setTextOn("Show");

        revView.setTextOff("Reverse");
        revView.setTextOn("Normal");

        if (isHomeApp()){
            showAppBar.setChecked(sharedPreferences.getBoolean("showAppBar",false));
            showClock.setChecked(sharedPreferences.getBoolean("showClock",false));
            revView.setChecked(sharedPreferences.getBoolean("revView",false));
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
            finish();
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
        revView.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("revView",revView.isChecked());
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
