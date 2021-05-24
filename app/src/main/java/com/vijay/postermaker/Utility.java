package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utility {

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

    Bitmap drawableToBitmap(int drawableId, Context context) {
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

}
