package com.dodotdo.youngs.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;

import com.dodotdo.youngs.R;

/**
 * Created by KimYebon on 16. 4. 11..
 */
public class Util {

    public static void makeDialogAndShow(String title, String description, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(description)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public static boolean[] intToBoolean(int teachingDay) {
        boolean[] selectedDay = new boolean[7];
        int unit = 1000000;

        for(int i=0 ;i<7; i++) {
            if(teachingDay-unit>=0) {
                selectedDay[i]=true;
                teachingDay-=unit;
            }else {
                selectedDay[i]=false;
            }
            unit/=10;
        }


        return selectedDay;
    }


}
