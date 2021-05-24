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

    Properties properties;
    Context context;
    Utility util;
    UserData userData;
    Bitmap latestSavedBitmap;

    public Poster(Context context, Bitmap posterBitmap, Bitmap faceBitmap) {
        this.context = context;
        userData = new UserData(context);
        util = new Utility();
        properties = Properties.build( posterBitmap);
        properties.setFace(faceBitmap); // important to do so
    }

    public Bitmap getAttachedName(){
        View withName = new NameAttach(context, getAttachedFace());
        latestSavedBitmap = viewToBitmap(withName, properties.posterWidth, properties.posterHeight);
        return latestSavedBitmap;
    }

    public Bitmap getAttachedFace() {
        View withFace = new FaceAttach(context, getAttachedFooter(), properties.face);
        return viewToBitmap(withFace, properties.posterWidth, properties.posterHeight);
    }

    public Bitmap getAttachedFooter() {
        View view = new FooterAttach(context, properties.poster, getCombinedFooter());
        return viewToBitmap(view, properties.posterWidth, properties.posterHeight);
    }

    public Bitmap getCombinedFooter() {

        List<Bitmap> list = new ArrayList<>(); // here we get bitmaps of each id
        list.add(getSingleFooter(userData.drawableWhsAp, userData.whatsAppInfo));
        list.add(getSingleFooter(userData.drawableInsta, userData.InstagramInfo));
        list.add(getSingleFooter(userData.drawableTwtr, userData.twitterInfo));
        list.add(getSingleFooter(userData.drawableFb, userData.facebookInfo));

        properties.setListSingleFooters(list); // This gives how much gape should be between the id's

        Bitmap combinedFooter = viewToBitmap(new FooterCombiner(context, list),
                properties.footerWidth, properties.footerHeight);

        // if gape is -ve means overlay,
        // so we decrease the ratio then create the single footers again
        //  till we reach the minimum gap (+ ve)
        if (properties.singleFooterGape < properties.minimumSingleFooterGape) {
            properties.reCalculateValues(0.1f);
            Log.d("SEET", "getCombinedFooter: " + properties.footerHeightRatioToPosterHeight);
            return getCombinedFooter();
        }

        return combinedFooter;
    }

    private Bitmap getSingleFooter(int iconDrawableId, String text) {
        View socialMedia = new FooterCreateSingle(context, iconDrawableId, text);
        return viewToBitmap(socialMedia, properties.currentSingleFooterWidth, properties.footerHeight);
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
