package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class FaceAttach extends View {

    Bitmap posterBitmap, faceBitmap;

    Properties properties;

    public FaceAttach(Context context) {
        super(context);
    }

    public FaceAttach(Context context, Bitmap poster, Bitmap face) {
        super(context);

        properties = Properties.getInstance();
        posterBitmap = poster;
        faceBitmap = getPerfectSize(face);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw the poster bitmap on canvas
        canvas.drawBitmap(posterBitmap, 0, 0, null);

        // now draw the face bitmap on canvas
        canvas.drawBitmap(faceBitmap, properties.faceDestinationX,
                properties.faceDestinationY, null);
    }

    Bitmap getPerfectSize(Bitmap face) {
        float ratio = properties.faceSizeRatioToPoster;

        float hPoster, wPoster;

        hPoster = posterBitmap.getHeight();
        wPoster = posterBitmap.getWidth();

        Bitmap resized = new Utility().resizeBitmap(
                face, hPoster / ratio, wPoster / ratio);

        properties.setFace(resized);

        return resized;
    }

}
