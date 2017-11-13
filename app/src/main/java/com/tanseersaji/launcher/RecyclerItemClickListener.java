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
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private RecyclerItemClickListener.OnItemClickListener mListener;

    public interface OnItemClickListener{
         void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener){
        mListener =  listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }


        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
        }
        return false;
    }
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

}
