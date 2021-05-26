package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class FaceAttach extends View {

    Bitmap posterBitmap, faceBitmap;

    PosterProperties posterProperties;

    public FaceAttach(Context context) {
        super(context);
    }

    public FaceAttach(Context context, Bitmap poster, Bitmap face) {
        super(context);

        posterProperties = PosterProperties.getInstance();
        posterBitmap = poster;
        faceBitmap = face;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw the poster bitmap on canvas
        canvas.drawBitmap(posterBitmap, 0, 0, null);

        // now draw the face bitmap on canvas
        canvas.drawBitmap(faceBitmap, posterProperties.faceDestinationX,
                posterProperties.faceDestinationY, null);
    }



}
