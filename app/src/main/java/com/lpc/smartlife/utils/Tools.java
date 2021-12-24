package com.lpc.smartlife.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Tools {

    public static void displayToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static Drawable makeDrawable(int id, AppCompatActivity a){
        Resources resources = a.getApplicationContext().getResources();
        Drawable drawable = resources.getDrawable(id);
        //前两个参数为X轴和Y轴的起点,后两个参数是宽高
        drawable.setBounds(1, 1, drawable.getMinimumHeight(), drawable.getMinimumWidth());
        return drawable;
    }
}
