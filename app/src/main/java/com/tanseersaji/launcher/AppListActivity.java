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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

import static com.tanseersaji.launcher.MainActivity.openApp;

public class AppListActivity extends Activity {

    private RecyclerView list;
    private AppListActivity context = AppListActivity.this;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);


        list = findViewById(R.id.list);

        AppListAdaptor adaptor = new AppListAdaptor( MainActivity.apps);
        list.setAdapter(adaptor);

        GridLayoutManager glm = new GridLayoutManager(this,3);
        list.setLayoutManager(glm);

        addClickListener();


        MobileAds.initialize(this, "ca-app-pub-6440435975761242~6952366562");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6440435975761242/7389751776");
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());


    }


    private void addClickListener(){
        list.addOnItemTouchListener(new RecyclerItemClickListener(this,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                SharedPreferences sharedPreferences = getSharedPreferences("NoLauncherSettings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (sharedPreferences.getInt("AdCount", 0) == 9) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        openApp(AppListActivity.this,  MainActivity.apps.get(position).getLabel().toString());
                    }

                    mInterstitialAd.setAdListener(new AdListener() {

                        @Override
                        public void onAdClosed() {
                            mInterstitialAd.loadAd(new AdRequest.Builder()
                                    .build());
                            openApp(AppListActivity.this,  MainActivity.apps.get(position).getLabel().toString());
                            MainActivity.apps.clear();
                            finish();
                        }
                    });

                }else{
                    openApp(AppListActivity.this,  MainActivity.apps.get(position).getLabel().toString());
                    MainActivity.apps.clear();
                    finish();
                }
                editor.putInt("AdCount",(sharedPreferences.getInt("AdCount", 0)+1)%10);
                editor.apply();
            }


        }));
    }

    private ArrayList<Item> getApps(){
        ArrayList<Item> appList = new ArrayList<Item>();
         Item it;

         for(int i = 0;i< MainActivity.apps.size();i++){
             it = new Item();
             it.setName( MainActivity.apps.get(i).getName());
             it.setIcon( MainActivity.apps.get(i).getIcon());
             it.setLabel( MainActivity.apps.get(i).getLabel());

             appList.add(it);

         }
        return appList;
    }



    public  Context getInstance(){
        return context;
    }

    }