package com.vijay.postermaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Poster {

    PosterProperties posterProperties;
    Context context;
    UserData userData;
    Bitmap latestSavedBitmap;

    public Poster(Context context) {
        this.context = context;
        userData = UserData.getInstance(context);
        posterProperties = PosterProperties.
                buildNewProperties(userData.getBitmapPoster());
        posterProperties.setFace(userData.getBitmapFace()); // important to do so
        latestSavedBitmap = getAttachedName();
    }

    public Bitmap getAttachedName() {
        View withName = new NameAttach(context, getAttachedFace());
        return viewToBitmap(withName, posterProperties.posterWidth, posterProperties.posterHeight);
    }

    private Bitmap getAttachedFace() {
        View withFace = new FaceAttach(context, getAttachedFooter(), posterProperties.face);
        return viewToBitmap(withFace, posterProperties.posterWidth, posterProperties.posterHeight);
    }

    private Bitmap getAttachedFooter() {
        View view = new FooterAttach(context, posterProperties.poster, getCombinedFooter());
        return viewToBitmap(view, posterProperties.posterWidth, posterProperties.posterHeight);
    }

    private Bitmap getCombinedFooter() {

        List<Bitmap> list = new ArrayList<>(); // here we get bitmaps of each id
        list.add(getSingleFooter(userData.getBmWhsap(), userData.whatsAppInfo));
        list.add(getSingleFooter(userData.getBmInsta(), userData.InstagramInfo));
        list.add(getSingleFooter(userData.getBmTwitr(), userData.twitterInfo));
        list.add(getSingleFooter(userData.getBmFB(), userData.facebookInfo));

        posterProperties.setListSingleFooters(list); // This gives how much gape should be between the id's

        Bitmap combinedFooter = viewToBitmap(new FooterCombiner(context, list),
                posterProperties.footerWidth, posterProperties.footerHeight);

        // if gape is -ve means overlay,
        // so we decrease the ratio then create the single footers again
        //  till we reach the minimum gap (+ ve)
        if (posterProperties.singleFooterGape < posterProperties.minimumSingleFooterGape) {
            posterProperties.reCalculateValues(0.5f);
            return getCombinedFooter();
        }

        return combinedFooter;
    }

    private Bitmap getSingleFooter(Bitmap iconBitmap, String text) {
        View socialMedia = new FooterCreateSingle(context, iconBitmap, text);
        return viewToBitmap(socialMedia, posterProperties.currentSingleFooterWidth, posterProperties.footerHeight);
    }


    private void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private Bitmap viewToBitmap(View view, float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap(
                (int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas); // this changes the bitmap
        return bitmap;
    }


}
