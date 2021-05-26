package com.vijay.postermaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

public class UserData {

    /* do not use this class directly  (use propertiesz instance instead)
     * Use posterProperties class to get any type of data,
     * Here we store user data which is saved in SharedPreferences
     * Like User details to show on the poster
     * Poster detailing like colors and background */

    /* Fields that are transient cannot be part of serialization (will not save in Gson) */
    private transient final String USER_DATA_SP = "userDataSP1";
    private transient final String USER_DATA_JSON = "userDataJson1";
    private transient SharedPreferences sharedPref;
    private transient SharedPreferences.Editor sPrefEditor;
    private static UserData userData;

    private transient Bitmap bmFB, bmInsta, bmTwitr, bmWhsap;

    String whatsAppInfo, facebookInfo, twitterInfo, InstagramInfo, fontChosenKey;
    private int drawableWhsAp, drawableFb, drawableInsta, drawableTwtr;

    private String name, userPostDetails,
            businessName, businessDetails, businessLocation,
            websiteUrl, businessEmail;

    private String faceByteArrayString, posterByteArrayString;


    /* NOTE: TO add more parameters
     *  Add them globally and then add them in
     *  METHOD -> setUserDataFromJson()
     *  METHOD -> initializeAllStrings()
     *  {above is necessary, else nullPointerException}
     *
     *  ALSO UPDATE -> toString (override Method) (this for us)
     *
     * To add new item like Snapchat Info, add that in the list in Poster Class
     * in method -> public Bitmap getCombinedFooter()
     * then also initialize it in constructor  */

    private UserData(Context context) {

        sharedPref = context.getSharedPreferences(USER_DATA_SP, Context.MODE_PRIVATE);
        sPrefEditor = sharedPref.edit();
        sPrefEditor.apply();

        initializeAllStrings(); // to prevent null exceptions

        String jsonString = sharedPref.getString(USER_DATA_JSON, "");

        // if there is no data then do nothing
        assert jsonString != null;
        if (!jsonString.equals("")) {

            Gson gson = new Gson();
            UserData userData = gson.fromJson(jsonString, UserData.class);
            setUserDataFromJson(userData);
            createSocialBitmaps(context); // we need this only once
            // now we have assigned previous (old) data to this object
        }
    }

    public static UserData getInstance(Context context) {
        if (userData == null) userData = new UserData(context.getApplicationContext());
        return userData;
    }

    public static UserData getInstance() {
        return userData; // WARNING: only when you are sure that it has been created already
    }

    public boolean canCreatePoster() {
        return posterByteArrayString != null && faceByteArrayString != null;
    }

    void initializeAllStrings() {

        name = "Vijay Jangid";
        userPostDetails = "Join Open Innovations";

        whatsAppInfo = "+91 9999988888";
        facebookInfo = "@facebook.com";
        twitterInfo = "@twitter.com";
        InstagramInfo = "@instagram.com";

        drawableWhsAp = R.drawable.whatsapp;
        drawableFb = R.drawable.facebook;
        drawableInsta = R.drawable.instagram;
        drawableTwtr = R.drawable.twitter;

        businessName = "Vijay Company";
        businessDetails = "Company Details";
        businessLocation = "Jaipur";
        businessEmail = "vijay@gmail.com";
        websiteUrl = "Website Url";
        faceByteArrayString = null;
        posterByteArrayString = null;

        fontChosenKey = "sans-serif-medium";
    }

    private void createSocialBitmaps(Context context) {

        bmFB = Utility.getInstance().resizeBitmapNormal(
                BitmapFactory.decodeResource(context.getResources(), drawableFb));
        bmInsta = Utility.getInstance().resizeBitmapNormal(
                BitmapFactory.decodeResource(context.getResources(), drawableInsta));
        bmTwitr = Utility.getInstance().resizeBitmapNormal(
                BitmapFactory.decodeResource(context.getResources(), drawableTwtr));
        bmWhsap = Utility.getInstance().resizeBitmapNormal(
                BitmapFactory.decodeResource(context.getResources(), drawableWhsAp));
    }

    public void applyUpdate(Context context) {

        /* TO UPDATE DATA FOLLOW THESE STEPS
         * 1. Create the object (created by old json automatically)
         * 2. Set the data you want to change
         * 3. Now call updateData from the object in which data changed
         * 4. We will save the data from that object in SharedPref
         * 5. HOW? updated strings gets replaced and others will be there as it is
         *  */

        sharedPref = context.getSharedPreferences(USER_DATA_SP,
                Context.MODE_PRIVATE);
        sPrefEditor = sharedPref.edit();
        sPrefEditor.putString(USER_DATA_JSON, new Gson().toJson(this));
        sPrefEditor.apply();
    }

    // internal used method
    private void setUserDataFromJson(UserData userData) {

        this.businessEmail = userData.businessEmail;
        this.websiteUrl = userData.websiteUrl;
        this.businessLocation = userData.businessLocation;
        this.businessDetails = userData.businessDetails;
        this.businessName = userData.businessName;
        this.userPostDetails = userData.userPostDetails;
        this.name = userData.name;
        this.drawableTwtr = userData.drawableTwtr;
        this.InstagramInfo = userData.InstagramInfo;
        this.drawableWhsAp = userData.drawableWhsAp;
        this.drawableFb = userData.drawableFb;
        this.drawableInsta = userData.drawableInsta;
        this.twitterInfo = userData.twitterInfo;
        this.facebookInfo = userData.facebookInfo;
        this.whatsAppInfo = userData.whatsAppInfo;
        this.fontChosenKey = userData.fontChosenKey;
        this.faceByteArrayString = userData.faceByteArrayString;
        this.posterByteArrayString = userData.posterByteArrayString;

    }

    // Getter setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserPostDetails() {
        return userPostDetails;
    }

    public void setUserPostDetails(String userPostDetails) {
        this.userPostDetails = userPostDetails;
    }

    public String getWhatsAppInfo() {
        return whatsAppInfo;
    }

    public void setWhatsAppInfo(String whatsAppInfo) {
        this.whatsAppInfo = whatsAppInfo;
    }

    public String getFacebookInfo() {
        return facebookInfo;
    }

    public void setFacebookInfo(String facebookInfo) {
        this.facebookInfo = facebookInfo;
    }

    public String getTwitterInfo() {
        return twitterInfo;
    }

    public void setTwitterInfo(String twitterInfo) {
        this.twitterInfo = twitterInfo;
    }

    public String getInstagramInfo() {
        return InstagramInfo;
    }

    public void setInstagramInfo(String instagramInfo) {
        InstagramInfo = instagramInfo;
    }

    public int getDrawableFb() {
        return drawableFb;
    }

    public void setDrawableFb(int drawableFb) {
        this.drawableFb = drawableFb;
    }

    public int getDrawableInsta() {
        return drawableInsta;
    }

    public void setDrawableInsta(int drawableInsta) {
        this.drawableInsta = drawableInsta;
    }

    public int getDrawableTwtr() {
        return drawableTwtr;
    }

    public void setDrawableTwtr(int drawableTwtr) {
        this.drawableTwtr = drawableTwtr;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(String businessDetails) {
        this.businessDetails = businessDetails;
    }

    public String getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(String businessLocation) {
        this.businessLocation = businessLocation;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public Bitmap getBmFB() {
        return bmFB;
    }

    public Bitmap getBmInsta() {
        return bmInsta;
    }

    public Bitmap getBmTwitr() {
        return bmTwitr;
    }

    public Bitmap getBmWhsap() {
        return bmWhsap;
    }

    public Bitmap getBitmapFace() {
        if (faceByteArrayString == null) return null;
        return toBitmap(faceByteArrayString);
    }

    public Bitmap getBitmapPoster() {
        if (posterByteArrayString == null) return null;
        return toBitmap(posterByteArrayString);
    }


    public void setFaceByteArrayString(Bitmap bitmapFace) {
        this.faceByteArrayString = getBase64String(bitmapFace);
    }

    public void setPosterByteArrayString(Bitmap bitmapPoster) {
        this.posterByteArrayString = getBase64String(bitmapPoster);
    }

    private String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return android.util.Base64.encodeToString(imageBytes,
                Base64.DEFAULT);
    }

    private Bitmap toBitmap(String base64String) {
        byte[] decodedByteArray = android.util.
                Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray,
                0, decodedByteArray.length);
    }
}