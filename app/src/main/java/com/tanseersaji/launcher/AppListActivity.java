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
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.tanseersaji.launcher.MainActivity.openApp;

public class AppListActivity extends Activity {

    private RecyclerView list;
    private InterstitialAd mInterstitialAd;
    private ArrayList<ResolveInfo> apps = new ArrayList<ResolveInfo>();
    private static PackageManager manager;
    LinearLayout appDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        appDrawerLayout = findViewById(R.id.appDrawerLayout);

        list = findViewById(R.id.list);

        long t0 = System.currentTimeMillis();
        getAppsList();
        AppListAdaptor adaptor = new AppListAdaptor(apps);
        list.setAdapter(adaptor);
        GridLayoutManager glm = new GridLayoutManager(this, 3);
        list.setLayoutManager(glm);
        Log.i("PLUM",String.valueOf((System.currentTimeMillis()-t0)));

        addClickListener();

        MobileAds.initialize(this, "ca-app-pub-6440435975761242~6952366562");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6440435975761242/7389751776");
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());


        appDrawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void addClickListener() {

        list.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                SharedPreferences sharedPreferences = getSharedPreferences("NoLauncherSettings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (sharedPreferences.getInt("AdCount", 0) == 9) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        openApp(AppListActivity.this, apps.get(position).activityInfo.packageName);
                    }

                    mInterstitialAd.setAdListener(new AdListener() {

                        @Override
                        public void onAdClosed() {
                            mInterstitialAd.loadAd(new AdRequest.Builder()
                                    .build());
                            openApp(AppListActivity.this, apps.get(position).activityInfo.packageName);
                        }
                    });

                } else {
                    openApp(AppListActivity.this, apps.get(position).activityInfo.packageName);
                }
                editor.putInt("AdCount", (sharedPreferences.getInt("AdCount", 0) + 1) % 10);
                editor.apply();
            }


        }));
    }


    private void getAppsList(){

        manager = getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN,null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<ResolveInfo> availableActivities = (ArrayList<ResolveInfo>) manager.queryIntentActivities(i,0);
        apps = availableActivities;
        Collections.sort(apps, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo r1, ResolveInfo r2) {
                return r1.activityInfo.loadLabel(manager).toString().compareToIgnoreCase(r2.loadLabel(manager).toString());
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        PowerManager p = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            if(!p.isInteractive()){
                System.gc();
            }
        }else {
            if(!p.isScreenOn()){
                System.gc();
            }
        }
    }

    public static PackageManager getPM(){
        return manager;
    }
}