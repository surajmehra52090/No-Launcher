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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private InterstitialAd mInterstitialAd;
    Button contactButton;
    Button messageButton;
    Button googleButton;
    Button cameraButton;
    Button galleryButton;
    Button moreButton;
    Button playStoreBotton;
    Button wallBucketButton;
    Button cleanButton;
    LinearLayout appBar;
    LinearLayout dateTime;
    SimpleDateFormat simpleDateFormat;
    String time;
    String date;
    Calendar calander;
    Timer t = new Timer();
    TextView timeTextView;
    TextView dateTextView;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         timeTextView = findViewById(R.id.clockk);
         dateTextView = findViewById(R.id.date);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        t.scheduleAtFixedRate(new MyTimerClass(),0,1000);


        MobileAds.initialize(this, "ca-app-pub-6440435975761242~6952366562");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6440435975761242/3946041228");
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .build());
        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder()
                        .build());
                openApp(MainActivity.this,"ytstudios.wall.bucket");
            }
        });

         contactButton = (Button) findViewById(R.id.contact);
         messageButton = (Button) findViewById(R.id.message);
         googleButton = (Button) findViewById(R.id.google);
         cameraButton = (Button) findViewById(R.id.camera);
         galleryButton = (Button) findViewById(R.id.gallery);
         moreButton = (Button) findViewById(R.id.more);
         playStoreBotton = (Button) findViewById(R.id.playStore);
         wallBucketButton = (Button) findViewById(R.id.wallbucket);
         cleanButton = (Button) findViewById(R.id.clean);
         appBar = (LinearLayout) findViewById(R.id.appBar);
         dateTime = (LinearLayout) findViewById(R.id.dateTimeLayout);


        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                startActivity(i);
            }
        });
         cleanButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(MainActivity.this,HelperActivity.class);
                 startActivity(i);
             }
         });
        wallBucketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    openApp(MainActivity.this,"ytstudios.wall.bucket");
                }

            }
        });

        playStoreBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store"));
                startActivity(i);
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(MainActivity.this,"com.google.android.googlequicksearchbox");
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AppListActivity.class);
                startActivity(intent);
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                startActivity(intent);
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivity(galleryIntent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("NoLauncherSettings", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("showAppBar",false)){

            appBar.setBackgroundColor(getResources().getColor(R.color.appBarColor));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                appBar.setElevation(4);
            }
        }
        else{
            appBar.setBackgroundColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                appBar.setElevation(0);
            }
        }
        if(sharedPreferences.getBoolean("showClock",false)){
            dateTime.setVisibility(View.GONE);
        }
        else {
            dateTime.setVisibility(View.VISIBLE);
        }

    }


    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                Intent it = new Intent(android.content.Intent.ACTION_VIEW);
                it.setData(Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
                context.startActivity(it);
                return false;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);

        } catch (ActivityNotFoundException e) {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
            context.startActivity(i);

        }
        return true;
    }


    private void setDateTime(){
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("h:mm a");
        time = simpleDateFormat.format(calander.getTime());
        simpleDateFormat = new SimpleDateFormat("EEE, dd MMM");
        date = simpleDateFormat.format(calander.getTime());
        timeTextView.setText(time);
        dateTextView.setText(date);
    }
    class MyTimerClass extends TimerTask{
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setDateTime();
                }
            });
        }
    }




}
