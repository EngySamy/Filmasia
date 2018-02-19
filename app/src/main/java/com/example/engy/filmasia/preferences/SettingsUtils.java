package com.example.engy.filmasia.preferences;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import com.example.engy.filmasia.R;

/**
 * Created by Engy on 2/18/2018.
 */

public class SettingsUtils {
    private static boolean SHOW_YEAR;
    @ColorInt
    private static int color;
    private static float size;

    public static void setShowYear(boolean showYear){
        SHOW_YEAR=showYear;
    }
    public static boolean getShowYear(){
        return SHOW_YEAR;
    }

    public static void setColor(String color1,Context context){
        if(color1.equals(context.getString(R.string.pref_black_color_value)))
            color= ContextCompat.getColor(context,R.color.black);
        else if(color1.equals(context.getString(R.string.pref_red_color_value)))
            color= ContextCompat.getColor(context,R.color.red);
        else if(color1.equals(context.getString(R.string.pref_blue_color_value)))
            color= ContextCompat.getColor(context,R.color.blue);
        else if(color1.equals(context.getString(R.string.pref_green_color_value)))
            color= ContextCompat.getColor(context,R.color.green);
    }
    public static int getColor(){
        return color;
    }

    public static void setSize(float size1){
        size=size1;
    }
    public static float getSize(){
        return size;
    }
}
