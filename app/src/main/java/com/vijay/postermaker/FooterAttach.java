package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class FooterAttach extends View {

    Bitmap posterBitmap;
    Bitmap footerBitmap;
    PosterProperties posterProperties;

    public FooterAttach(Context context) {
        super(context);
    }

    public FooterAttach(Context context, Bitmap poster, Bitmap footer) {
        super(context);

        posterProperties = PosterProperties.getInstance();
        footerBitmap = footer;
        posterBitmap = poster;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // drawing the poster in this canvas
        canvas.drawBitmap(posterBitmap, 0, 0, null);
        int originalHeight = posterBitmap.getHeight();

        // drawing footer to this canvas (type - overlay)
        canvas.drawBitmap(footerBitmap, 0, originalHeight -
                footerBitmap.getHeight() , null);
    }
}
