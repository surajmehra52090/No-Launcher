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
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AppListAdaptor extends RecyclerView.Adapter<AppListAdaptor.MyViewHolder>  {

    ArrayList<Item> apps;
    Context context;

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

    public AppListAdaptor(ArrayList<Item> apps){
        this.apps = apps;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item app = apps.get(position);
        holder.name.setText(app.getName());
        holder.icon.setImageDrawable(app.getIcon());

        holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                    Uri packageURI = Uri.parse("package:" + apps.get(position).getLabel().toString());
                    Intent i = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI);
                    context.startActivity(i);

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }



}
