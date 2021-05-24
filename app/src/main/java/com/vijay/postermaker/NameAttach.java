package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class NameAttach extends View {

    Bitmap posterBitmap;
    Paint namePaint;
    Properties properties;
    UserData userData;

    public NameAttach(Context context, Bitmap bitmap) {
        super(context);
        posterBitmap = bitmap;
        userData = new UserData(context);
        properties = Properties.getInstance();

        namePaint = new Paint();

        namePaint.setTextSize(properties.nameTextSize);
        namePaint.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw the poster bitmap on canvas (this is blank that's why)
        canvas.drawBitmap(posterBitmap, 0, 0, null);

        canvas.drawText(userData.getName(), properties.paddingPoster,
               properties.posterHeight - properties.footerHeight
                - properties.paddingPoster, namePaint);
    }

}
