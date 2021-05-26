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
    PosterProperties posterProperties;
    UserData userData;

    public NameAttach(Context context, Bitmap bitmap) {
        super(context);
        posterBitmap = bitmap;
        userData = UserData.getInstance();
        posterProperties = PosterProperties.getInstance();

        namePaint = new Paint();
        namePaint.setTextSize(posterProperties.nameTextSize);
        namePaint.setColor(Color.WHITE);
        namePaint.setTypeface(Utility.getInstance()
                .getSystemFontList().get(posterProperties.fontChosenKey));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw the poster bitmap on canvas (this is blank that's why)
        canvas.drawBitmap(posterBitmap, 0, 0, null);

        canvas.drawText(userData.getName(), posterProperties.paddingPoster,
                posterProperties.posterHeight - posterProperties.footerHeight
                        - posterProperties.paddingPoster, namePaint);
    }

}
