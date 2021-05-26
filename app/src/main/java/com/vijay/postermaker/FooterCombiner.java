package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class FooterCombiner extends View {

    float nextPointOnXaxis;

    List<Bitmap> listBitmap;
    PosterProperties posterProperties;

    Paint backgroundPaint;

    public FooterCombiner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterCombiner(Context context, List<Bitmap> list) {
        super(context);
        listBitmap = list;
        posterProperties = PosterProperties.getInstance();

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPaint(backgroundPaint);

        nextPointOnXaxis = posterProperties.footerPadding;

        // here without left,right padding

        for (int i = 0; i < listBitmap.size(); i++) {
            canvas.drawBitmap(listBitmap.get(i), nextPointOnXaxis, 0, null);
            nextPointOnXaxis += listBitmap.get(i).getWidth() + posterProperties.singleFooterGape;
        }

    }
}
