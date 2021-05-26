package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import java.lang.reflect.Field;
import java.util.Map;

public class Utility {

    private static Utility utility;

    // Keeps aspect ratio
    Bitmap resizeByWidth(Bitmap original, float toWidth) {
        float hOriginal = original.getHeight(), wOriginal = original.getWidth();
        float percentChanged = toWidth / wOriginal;
        float newHeight = percentChanged * hOriginal + 1;
        return Bitmap.createScaledBitmap(original, (int) toWidth, (int) newHeight, false);
    }

    // Keeps aspect ratio
    Bitmap resizeByHeight(Bitmap original, float toHeight) {
        // 1000/500 -> 2,
        float hOriginal = original.getHeight(), wOriginal = original.getWidth();
        float percentChanged = toHeight / hOriginal;
        float newWidth = percentChanged * wOriginal;
        return Bitmap.createScaledBitmap(original, (int) newWidth, (int) toHeight, false);
    }

    Bitmap resizeBitmap(Bitmap original, float toHeight, float toWidth) {
        // resize as the smaller we want
        if (toHeight < toWidth) return resizeByHeight(original, toHeight);
        else return resizeByWidth(original, toWidth);
    }

    Bitmap resizeBitmapNormal(Bitmap original) {
        // for making bitmaps normal
        return resizeBitmap(original, 1920, 1080);
    }

    Bitmap drawableToBitmap(int drawableId, Context context) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

    private Utility() {}

    public static Utility getInstance() {
        if (utility == null) utility = new Utility();
        return utility;
    }

    public Map<String, Typeface> getSystemFontList() {

        // Here we get the fonts available in the application
        // How to add new
        // Go to a textview and then set font then More fonts...
        // then add the fonts that you want to add

        Map<String, Typeface> sSystemFontMap = null;
        try {
            //Typeface typeface = Typeface.class.newInstance();
            Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
            Field f = Typeface.class.getDeclaredField("sSystemFontMap");
            f.setAccessible(true);
            sSystemFontMap = (Map<String, Typeface>) f.get(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sSystemFontMap;
    }


}
