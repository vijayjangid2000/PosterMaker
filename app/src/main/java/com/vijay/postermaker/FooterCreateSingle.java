package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

public class FooterCreateSingle extends View {

    String TAG = "tag";

    String myText; // Example - OiLab@Facebook";
    int drawableId; // Example - R.drawable.facebook;

    float canvasPaddingHeight, canvasHeight, canvasWidth,
            iconWidth, iconHeight,
            textSize, textCenterVertically;

    Paint pBackground, pText;
    Bitmap icon;
    Context context;
    Properties properties;

    int finalWidth = 0;

    public FooterCreateSingle(Context context, int iconDrawableId, String text) {
        super(context);
        this.context = context;

        properties = Properties.getInstance();
        iconWidth = properties.iconHeightWidth;
        iconHeight = properties.iconHeightWidth;
        canvasPaddingHeight = properties.footerPadding;
        canvasHeight = properties.footerHeight;
        textSize = properties.textSize;
        textCenterVertically = (canvasHeight + textSize) / 2.1f; // for mid

        canvasWidth = properties.footerWidth; // we have to manually calculate

        pBackground = new Paint();
        pBackground.setColor(Color.WHITE);

        myText = text;
        drawableId = iconDrawableId;
        icon = BitmapFactory.decodeResource(getResources(), drawableId);
        icon = Bitmap.createScaledBitmap(icon,(int) iconWidth,(int) iconHeight, false);

        pText = new Paint();
        pText.setColor(Color.BLACK);
        pText.setTextSize(textSize);
        Typeface typeface = Typeface.SANS_SERIF;
        finalWidth += pText.measureText(myText) + 1;
        pText.setTypeface(typeface);

        finalWidth += iconWidth + properties.paddingIconAndText;
        properties.currentSingleFooterWidth = finalWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // setBackground
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, pBackground);

        // set icon (bitmap)
        canvas.drawBitmap(icon, 0, canvasPaddingHeight, null);

        // set Text
        canvas.drawText(myText, iconWidth + properties.paddingIconAndText, textCenterVertically, pText);
    }

}
