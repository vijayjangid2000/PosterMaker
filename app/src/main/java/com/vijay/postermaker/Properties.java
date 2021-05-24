package com.vijay.postermaker;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.List;

public class Properties {

    /* This class is dynamic, nothing is stored in it.
     * To resize the face image, footer length, text size or icon size or padding
     * Change the ratio below, these are ratio to poster.,
     * Like footerHeightRatio = 16 means footer is 1/16 times of poster height */

    float footerHeightRatioToPosterHeight = 16; //  this is dynamic, so no matter what value is here
    final float footerPaddingRatioToFooterHeight = 10;
    final float posterPaddingRatioToWidth = 28; // padding is 1/x times poster width
    final float textSizeRatioToFooter = 2; // textSize is 1/x times footer height
    final float iconHeightRatioToFooter = 1;  // 1/x times footer height
    final float singleFooterGapeRatioToIconHeight = 1.5f; // 1/x times of icon width
    final float nameTextSizeRationToFooterText = 3f; // means 3 times size of footer text
    final float faceSizeRatioToPoster = 3; // face size is 1/3 times of poster

    // Do not make changes below for resizing the image
    private static Properties properties; // for making it singleton

    List<Bitmap> listSingleFooters;
    Bitmap poster, face;

    float posterHeight, posterWidth;
    float footerHeight, footerWidth, footerPadding;
    float iconHeightWidth, singleFooterGape, paddingIconAndText, textSize;
    float sumSingleFootersWidth = 0; // not ready in constructor
    float minimumSingleFooterGape;
    float nameTextSize, paddingPoster;

    float faceHeight, faceWidth;
    float faceDestinationX, faceDestinationY;

    float currentSingleFooterWidth = 0;

    private Properties(Bitmap poster) {
        //this.context = context;
        this.poster = poster;
        calculateValues();
    }

    private void calculateValues() {
        posterHeight = poster.getHeight();
        posterWidth = poster.getWidth();

        footerHeight = posterHeight / footerHeightRatioToPosterHeight;
        footerWidth = posterWidth;

        footerPadding = footerHeight / footerPaddingRatioToFooterHeight;
        iconHeightWidth = (footerHeight / iconHeightRatioToFooter - (2 * footerPadding));
        textSize = footerHeight / textSizeRatioToFooter;
        paddingIconAndText = iconHeightWidth / footerPaddingRatioToFooterHeight;

        minimumSingleFooterGape = iconHeightWidth / singleFooterGapeRatioToIconHeight;
        nameTextSize = textSize * nameTextSizeRationToFooterText;
        paddingPoster = posterWidth/posterPaddingRatioToWidth;
    }

    // After the single footers created, pass the list here
    // then it becomes ready for merging
    public void setListSingleFooters(List<Bitmap> listSingleFooters) {
        this.listSingleFooters = listSingleFooters;

        sumSingleFootersWidth = 0;

        for (int i = 0; i < listSingleFooters.size(); i++) {
            Bitmap bitmap = listSingleFooters.get(i);
            sumSingleFootersWidth += bitmap.getWidth();
            Log.d("VJ W", "Width of each footer: "
                    + i + " - " + bitmap.getWidth());
        }

        singleFooterGape = ((posterWidth - sumSingleFootersWidth - (2 * footerPadding))
                / (listSingleFooters.size() - 1));


        Log.d("VJ gape", "Gape of each footer: " + singleFooterGape);
    }

    // After attaching footer call setFace
    public void setFace(Bitmap face) {
        this.face = face;

        faceHeight = face.getHeight();
        faceWidth = face.getWidth();

        faceDestinationX = posterWidth - faceWidth - paddingPoster;
        faceDestinationY = posterHeight - footerHeight - faceHeight - paddingPoster;
    }



    public static Properties getInstance() {
        return properties; // make sure to create a object before
    }

    public static Properties build(Bitmap poster) {
        if (properties == null) properties = new Properties(poster);
        return properties; // make sure to create a object before
    }

    // If we set this then all things changes with that
    public void reCalculateValues(float increaseRatioBy) {
        this.footerHeightRatioToPosterHeight += increaseRatioBy;
        calculateValues();
    }
}

