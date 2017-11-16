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
import android.os.PowerManager;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import hotchemi.android.rate.AppRate;


public class MainActivity extends Activity {

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
    LinearLayout parentLayout;
    LinearLayout dateTime;
    SimpleDateFormat simpleDateFormat;
    String time;
    String date;
    Calendar calander;
    Timer t = new Timer();
    TextView timeTextView;
    TextView dateTextView;
    SharedPreferences sharedPreferences;
    ArrayList<View> viewsMainScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         timeTextView = findViewById(R.id.clockk);
         dateTextView = findViewById(R.id.date);

        sharedPreferences = getSharedPreferences("NoLauncherSettings", Context.MODE_PRIVATE);
        t.scheduleAtFixedRate(new MyTimerClass(),0,1000);

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
         parentLayout = (LinearLayout) findViewById(R.id.parentLayout);

        Sensey.getInstance().init(MainActivity.this);


        AppRate.with(this)
                .setInstallDays(3)
                .setLaunchTimes(20)
                .setRemindInterval(2)
                .setShowLaterButton(true)
                .setShowNeverButton(true)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(MainActivity.this);

        new LovelyInfoDialog(MainActivity.this)
                .setTitle("No Launcher Tutorial")
                .setMessage("1. Swipe Up to open App Drawer\n2. You can customize Gesture Apps by long pressing the App Icon in the app drawer and choosing appropriate option\n" +
                        "You can change Gesture Apps by selecting another app for a particular Gesture")
                .setIcon(getResources().getDrawable(R.drawable.logo))
                .setTopColor(getResources().getColor(R.color.colorPrimaryDark))
                .setNotShowAgainOptionEnabled(0)
                .setNotShowAgainOptionChecked(false)
                .show();

        viewsMainScreen = new ArrayList<View>();
        for (int x = 0; x < parentLayout.getChildCount(); x++) {
            viewsMainScreen.add(parentLayout.getChildAt(x));
        }

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
                openApp(MainActivity.this,"ytstudios.wall.bucket");

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

        TouchTypeDetector.TouchTypListener touchTypeDetector = new TouchTypeDetector.TouchTypListener(){

            @Override
            public void onTwoFingerSingleTap() {
                if(sharedPreferences.contains("sia2")){
                    Log.i("PLUM",sharedPreferences.getString("sia2",""));
                    openApp(MainActivity.this,sharedPreferences.getString("sia2",""));
                }
            }

            @Override
            public void onThreeFingerSingleTap() {


            }

            @Override
            public void onDoubleTap() {
                if(sharedPreferences.contains("sia1")){
                    Log.i("PLUM",sharedPreferences.getString("sia1",""));
                    openApp(MainActivity.this,sharedPreferences.getString("sia1",""));
                }
            }

            @Override
            public void onScroll(int i) {

            }

            @Override
            public void onSingleTap() {

            }

            @Override
            public void onSwipe(int i) {
                if(i == TouchTypeDetector.SWIPE_DIR_UP){
                    Intent intent = new Intent(getApplicationContext(),AppListActivity.class);
                    startActivity(intent);
                }
                if(i == TouchTypeDetector.SWIPE_DIR_RIGHT){
                    if(sharedPreferences.contains("rsa")){
                        Log.i("PLUM",sharedPreferences.getString("rsa",""));
                        openApp(MainActivity.this,sharedPreferences.getString("rsa",""));
                    }
                }
                if(i == TouchTypeDetector.SWIPE_DIR_LEFT){
                    if(sharedPreferences.contains("lsa")){
                        Log.i("PLUM",sharedPreferences.getString("lsa",""));
                        openApp(MainActivity.this,sharedPreferences.getString("lsa",""));
                    }
                }
                if(i == TouchTypeDetector.SWIPE_DIR_DOWN){
                    if(sharedPreferences.contains("dsa")){
                        Log.i("PLUM",sharedPreferences.getString("dsa",""));
                        openApp(MainActivity.this,sharedPreferences.getString("dsa",""));
                    }
                }
            }

            @Override
            public void onLongPress() {

            }
        };

        Sensey.getInstance().startTouchTypeDetection(MainActivity.this,touchTypeDetector);
    }
    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        // Setup onTouchEvent for detecting type of touch gesture
        Sensey.getInstance().setupDispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }



    @Override
    protected void onPause() {
        super.onPause();
        PowerManager powerManager = (PowerManager)MainActivity.this.getSystemService(Context.POWER_SERVICE);
        Boolean isSceenAwake = (Build.VERSION.SDK_INT < 20? powerManager.isScreenOn():powerManager.isInteractive());

        if(!isSceenAwake){
            System.gc();
            Sensey.getInstance().stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.gc();
        Sensey.getInstance().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();

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

            if(sharedPreferences.getBoolean("revView",false)) {
                parentLayout.removeAllViews();
                for (int x = viewsMainScreen.size() - 1; x >= 0; x--) {
                    parentLayout.addView(viewsMainScreen.get(x));
                }
            }
            else{
                parentLayout.removeAllViews();
                for (int x = 0; x < viewsMainScreen.size(); x++) {
                    parentLayout.addView(viewsMainScreen.get(x));
                }

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

    @Override
    public void onBackPressed() {

    }


}
