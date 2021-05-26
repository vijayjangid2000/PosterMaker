package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class FooterCreateSingle extends View {

    String TAG = "tag";

    String myText; // Example - OiLab@Facebook";
    Bitmap iconBitmap; // Example - R.drawable.facebook;

    float canvasPaddingHeight, canvasHeight, canvasWidth,
            iconWidth, iconHeight,
            textSize, textCenterVertically;

    Paint pBackground, pText;
    Context context;
    PosterProperties posterProperties;

    int finalWidth = 0;

    public FooterCreateSingle(Context context, Bitmap iconBitmap, String text) {
        super(context);
        this.context = context;

        posterProperties = PosterProperties.getInstance();
        iconWidth = posterProperties.iconHeightWidth;
        iconHeight = posterProperties.iconHeightWidth;
        canvasPaddingHeight = posterProperties.footerPadding;
        canvasHeight = posterProperties.footerHeight;
        textSize = posterProperties.textSize;
        textCenterVertically = (canvasHeight + textSize) / 2.1f; // for mid

        canvasWidth = posterProperties.footerWidth; // we have to manually calculate

        pBackground = new Paint();
        pBackground.setColor(Color.WHITE);

        myText = text;
        this.iconBitmap = Bitmap.createScaledBitmap(iconBitmap
                , (int) iconWidth, (int) iconHeight, false);

        pText = new Paint();
        pText.setColor(Color.BLACK);
        pText.setTextSize(textSize);
        Typeface typeface = Utility.getInstance()
                .getSystemFontList().get(UserData.getInstance().fontChosenKey);

        finalWidth += pText.measureText(myText) + 1;
        pText.setTypeface(typeface);

        finalWidth += iconWidth + posterProperties.paddingIconAndText;
        posterProperties.currentSingleFooterWidth = finalWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // setBackground
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, pBackground);

        // set icon (bitmap)
        canvas.drawBitmap(iconBitmap, 0, canvasPaddingHeight, null);

        // set Text
        canvas.drawText(myText, iconWidth + posterProperties.paddingIconAndText, textCenterVertically, pText);
    }

}
