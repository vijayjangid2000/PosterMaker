package com.vijay.postermaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.google.gson.Gson;

public class UserData {

    /* Fields that are transient cannot be part of serialization (will not save in Gson) */
    private transient final String USER_DATA_SP = "userDataSP1";
    private transient final String USER_DATA_JSON = "userDataJson1";
    private transient SharedPreferences sharedPref;
    private transient SharedPreferences.Editor sPrefEditor;
    private transient Context context;
    private transient Bitmap bitmapFace, bitmapBusIcon;

    String whatsAppInfo, facebookInfo, twitterInfo, InstagramInfo;
    int drawableWhsAp, drawableFb, drawableInsta, drawableTwtr;

    String name, userPostDetails,
            businessName, businessDetails, businessLocation,
            websiteUrl, businessEmail;

    String decodableFaceString, decodableBusinessString;

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

    public UserData(Context context) {

        this.context = context;
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
            // now we have assigned previous (old) data to this object
        } else {
            Toast.makeText(context, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
    }

    public void applyUpdate() {

        /* TO UPDATE DATA FOLLOW THESE STEPS
         * 1. Create the object (created by old json automatically)
         * 2. Set the data you want to change
         * 3. Now call updateData from the object in which data changed
         * 4. We will save the data from that object in SharedPref
         * 5. HOW? updated strings gets replaced and others will be there as it is
         *  */

        sharedPref = context.getSharedPreferences(USER_DATA_SP, Context.MODE_PRIVATE);
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
        this.decodableFaceString = userData.decodableFaceString;
        this.decodableBusinessString = userData.decodableBusinessString;

        // these are transient -> not saved in sharedPref
        this.bitmapBusIcon = new Utility().resizeBitmap(BitmapFactory.
                decodeFile(decodableBusinessString), 1920, 1080);
        this.bitmapFace = new Utility().resizeBitmap(BitmapFactory.
                decodeFile(decodableFaceString), 1920, 1080);
    }

    void initializeAllStrings() {

        name = "Vijay Jangid";
        userPostDetails = "Join Open Innovations";

        whatsAppInfo = "+91 8953829329";
        facebookInfo = "facebook.com";
        twitterInfo = "twitter.com";
        InstagramInfo = "instagram.com";

        drawableWhsAp = R.drawable.whatsapp;
        drawableFb = R.drawable.facebook;
        drawableInsta = R.drawable.instagram;
        drawableTwtr = R.drawable.twitter;

        businessName = "Vijay Company";
        businessDetails = "Company Details";
        businessLocation = "Jaipur";
        businessEmail = "vijay@gmail.com";
        websiteUrl = "Website Url";

        decodableFaceString = "anything";
        decodableBusinessString = "anything";
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

    public int getDrawableWhsAp() {
        return drawableWhsAp;
    }

    public void setDrawableWhsAp(int drawableWhsAp) {
        this.drawableWhsAp = drawableWhsAp;
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

    public String getDecodableFaceString() {
        return decodableFaceString;
    }

    public void setDecodableFaceString(String decodableFaceString) {
        this.decodableFaceString = decodableFaceString;
    }

    public String getDecodableBusinessString() {
        return decodableBusinessString;
    }

    public void setDecodableBusinessString(String decodableBusinessString) {
        this.decodableBusinessString = decodableBusinessString;
    }

    public Bitmap getBitmapFace() {
        return bitmapFace;
    }

    public Bitmap getBitmapBusIcon() {
        return bitmapBusIcon;
    }
}
