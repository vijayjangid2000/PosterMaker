package com.vijay.postermaker;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.List;

public class PosterProperties {

    /* VERY IMP: After setting poster, also do setFace to get dimensions for face */

    /*This class do not have any relation  to UserData class */

    /* This class is dynamic, nothing is stored in it for permanent.
     * To resize the face image, footer length, text size or icon size or padding
     * Change the ratio below, these are ratio to poster.,
     * Like footerHeightRatio = 16 means footer is 1/16 times of poster height */

    float footerHeightRatioToPosterHeight = 16; //  this is dynamic, so no matter what value is here
    final float footerPaddingRatioToFooterHeight = 10;
    final float posterPaddingRatioToWidth = 32; // padding is 1/x times poster width
    final float textSizeRatioToFooter = 2; // textSize is 1/x times footer height
    final float iconHeightRatioToFooter = 1;  // 1/x times footer height
    final float singleFooterGapeRatioToIconHeight = 1.5f; // 1/x times of icon width
    final float nameTextSizeRationToFooterText = 3f; // means 3 times size of footer text
    final float faceSizeRatioToPoster = 3; // face size is 1/3 times of poster

    // Do not make changes below for resizing the image
    private static PosterProperties posterProperties; // for making it singleton

    List<Bitmap> listSingleFooters;
    Bitmap poster, face;
    String fontChosenKey;

    float posterHeight, posterWidth;
    float footerHeight, footerWidth, footerPadding;
    float iconHeightWidth, singleFooterGape, paddingIconAndText, textSize;
    float sumSingleFootersWidth = 0; // not ready in constructor
    float minimumSingleFooterGape;
    float nameTextSize, paddingPoster;

    float faceHeight, faceWidth;
    float faceDestinationX, faceDestinationY;

    float currentSingleFooterWidth = 0;

    private PosterProperties(Bitmap poster) {
        //this.context = context;
        this.poster = poster;
        try {
            calculateValues();
        } catch (Exception e) {
            Log.d("Error Log", "PosterProperties: " +
                    "This is happening because the poster bitmap or Face bitmap is Null" +
                    ". You should check the bitmaps where you are creating the object of this class.");
            e.printStackTrace();
        }
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
        paddingPoster = posterWidth / posterPaddingRatioToWidth;
    }

    // After the single footers created, pass the list here
    // then it becomes ready for merging
    public void setListSingleFooters(List<Bitmap> listSingleFooters) {
        this.listSingleFooters = listSingleFooters;

        sumSingleFootersWidth = 0;

        for (int i = 0; i < listSingleFooters.size(); i++) {
            Bitmap bitmap = listSingleFooters.get(i);
            sumSingleFootersWidth += bitmap.getWidth();
        }

        singleFooterGape = ((posterWidth - sumSingleFootersWidth - (2 * footerPadding))
                / (listSingleFooters.size() - 1));

        Log.d("Resizing Text: ", "Gape Increased To: " + singleFooterGape);
    }

    // After attaching footer call setFace
    public void setFace(Bitmap face) {
        try {
            this.face = face;

            faceHeight = face.getHeight();
            faceWidth = face.getWidth();

            faceDestinationX = posterWidth - faceWidth - paddingPoster;
            faceDestinationY = posterHeight - footerHeight - faceHeight;
        }catch (Exception e){
            Log.d("Error: ", "setFace: " + "Reason: Because the face image bitmap" +
                    "is null here. PLease check where are you calling the setFace() method" +
                    "and check if the bitmap is not null" );
            e.printStackTrace();
        }
    }


    public static PosterProperties getInstance() {
        return posterProperties; // make sure to create a object before
    }

    protected static PosterProperties buildNewProperties(Bitmap poster) {
        posterProperties = new PosterProperties(poster);
        return posterProperties; // make sure to create a object before
    }

    // If we set this then all things changes with that
    public void reCalculateValues(float increaseRatioBy) {
        this.footerHeightRatioToPosterHeight += increaseRatioBy;
        calculateValues();
    }
}

