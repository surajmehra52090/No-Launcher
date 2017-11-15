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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;


public class AppListAdaptor extends RecyclerView.Adapter<AppListAdaptor.MyViewHolder>  {

    ArrayList<ResolveInfo> apps;
    Context context;
    String[] setting = {"Set as Double Tap App","Set as Two Finger App","Set as Right Swipe App","Set as Left Swipe App","Set as Down Swipe App","Uninstall"};
    SharedPreferences sharedPreferences;
    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView icon;
        public View v;
        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            context = itemView.getContext();
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
        }
    }

    public AppListAdaptor(ArrayList<ResolveInfo> apps){
        this.apps = apps;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ResolveInfo app = apps.get(position);
        holder.name.setText(app.activityInfo.loadLabel(AppListActivity.getPM()));
        Glide.with(context)
                .load(app.activityInfo.loadIcon(AppListActivity.getPM()))
                .into(holder.icon);

        holder.v.setOnLongClickListener(view -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("NoLauncherSettings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            new LovelyChoiceDialog(context)
                    .setTitle("App Settings")
                    .setIcon(context.getResources().getDrawable(R.drawable.settings))
                    .setIconTintColor(context.getResources().getColor(R.color.appBarColor))
                    .setTopColorRes(R.color.colorPrimaryDark)
                    .setItems(setting, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(int p, String item) {
                            if(item.equalsIgnoreCase("Set as Double Tap App")){
                                editor.putString("sia1",apps.get(position).activityInfo.packageName);
                                editor.apply();
                            }
                            else if(item.equalsIgnoreCase("Set as Two Finger App")){
                                editor.putString("sia2",apps.get(position).activityInfo.packageName);
                                editor.apply();
                            } else if(item.equalsIgnoreCase("Set as Right Swipe App")){
                                editor.putString("rsa",apps.get(position).activityInfo.packageName);
                                editor.apply();
                            }
                            else if(item.equalsIgnoreCase("Set as Left Swipe App")){
                                editor.putString("lsa",apps.get(position).activityInfo.packageName);
                                editor.apply();
                            }
                            else if(item.equalsIgnoreCase("Set as Down Swipe App")){
                                editor.putString("dsa",apps.get(position).activityInfo.packageName);
                                editor.apply();
                            }
                            else if(item.equalsIgnoreCase("Uninstall")){
                                Uri packageURI = Uri.parse("package:" + apps.get(position).activityInfo.packageName);
                                Intent i = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI);
                                context.startActivity(i);
                            }
                        }
                    })
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }
}
