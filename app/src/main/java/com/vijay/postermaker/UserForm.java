package com.vijay.postermaker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class UserForm extends AppCompatActivity {

    final int RESULT_LOAD_IMG_1 = 1; // face
    final int RESULT_LOAD_IMG_2 = 2; // business

    TextInputLayout tilName, tilPostDetails, tilWhatsAppInfo, tilInstaInfo,
            tilTwitterInfo, tilFacebookInfo, tilWebsiteUrl,
            tilBusinessEmail, tilBusinessName, tilBusinessLocation;

    EditText etName, etPostDetails, etWhatsAppInfo, etInstaInfo,
            etTwitterInfo, etFacebookInfo, etWebsiteUrl,
            etBusinessEmail, etBusinessName, etBusinessLocation;

    TextView tvbSave, tvb_changeFace, tvb_changeBusinessIcon;
    UserData userData;

    String faceBitmapString, businessIconBitmapString;
    ImageView ivFace, ivBusinessIcon;

    Bitmap bitmapFace, bitmapBusinessIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        initViews();
    }

    void initViews() {

        userData = new UserData(this);

        tilName = findViewById(R.id.til_name);
        tilPostDetails = findViewById(R.id.til_post);
        tilWhatsAppInfo = findViewById(R.id.til_whatsApp);
        tilInstaInfo = findViewById(R.id.til_instagram);
        tilTwitterInfo = findViewById(R.id.til_twitter);
        tilFacebookInfo = findViewById(R.id.til_facebook);
        tilWebsiteUrl = findViewById(R.id.til_websiteUrl);
        tilBusinessEmail = findViewById(R.id.til_businessEmail);
        tilBusinessName = findViewById(R.id.til_businessName);
        tilBusinessLocation = findViewById(R.id.til_businessLocation);

        etName = findViewById(R.id.et_name);
        etPostDetails = findViewById(R.id.et_post);
        etWhatsAppInfo = findViewById(R.id.et_whatsApp);
        etInstaInfo = findViewById(R.id.et_instagram);
        etTwitterInfo = findViewById(R.id.et_twitter);
        etFacebookInfo = findViewById(R.id.et_facebook);
        etWebsiteUrl = findViewById(R.id.et_websiteUrl);
        etBusinessEmail = findViewById(R.id.et_businessEmail);
        etBusinessName = findViewById(R.id.et_businessName);
        etBusinessLocation = findViewById(R.id.et_businessLocation);

        ivFace = findViewById(R.id.iv_face);
        ivBusinessIcon = findViewById(R.id.iv_businessIcon);

        tvbSave = findViewById(R.id.tvb_save);
        tvb_changeBusinessIcon = findViewById(R.id.tvb_businessIcon);
        tvb_changeFace = findViewById(R.id.tvb_changeFaceImage);

        initListeners();
        setSavedInfo();
    }

    private void setSavedInfo() {
        etName.setText(userData.getName());
        etPostDetails.setText(userData.getUserPostDetails());
        etWhatsAppInfo.setText(userData.getWhatsAppInfo());
        etInstaInfo.setText(userData.getInstagramInfo());
        etTwitterInfo.setText(userData.getTwitterInfo());
        etFacebookInfo.setText(userData.getFacebookInfo());
        etWebsiteUrl.setText(userData.getWebsiteUrl());
        etBusinessEmail.setText(userData.getBusinessEmail());
        etBusinessName.setText(userData.getBusinessName());
        etBusinessLocation.setText(userData.getBusinessLocation());

        faceBitmapString = userData.getDecodableFaceString();
        businessIconBitmapString = userData.getDecodableBusinessString();

        if (faceBitmapString.length() > 10) {
            bitmapFace = new Utility().resizeBitmap(BitmapFactory
                    .decodeFile(faceBitmapString), 1920, 1080);
            ivFace.setImageBitmap(bitmapFace);
        }

        if (businessIconBitmapString.length() > 10) {
            bitmapBusinessIcon = new Utility().resizeBitmap(BitmapFactory
                    .decodeFile(businessIconBitmapString), 1920, 1080);
            ivBusinessIcon.setImageBitmap(bitmapBusinessIcon);
        }
    }

    void initListeners() {

        tvbSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });

        tvb_changeFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG_1);
            }
        });

        tvb_changeBusinessIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG_2);
            }
        });

    }

    void saveAndExit() {

        userData.setName(etName.getText().toString());
        userData.setUserPostDetails(etPostDetails.getText().toString());
        userData.setWhatsAppInfo(etWhatsAppInfo.getText().toString());
        userData.setInstagramInfo(etInstaInfo.getText().toString());
        userData.setTwitterInfo(etTwitterInfo.getText().toString());
        userData.setFacebookInfo(etFacebookInfo.getText().toString());
        userData.setWebsiteUrl(etWebsiteUrl.getText().toString());
        userData.setBusinessEmail(etBusinessEmail.getText().toString());
        userData.setBusinessName(etBusinessName.getText().toString());
        userData.setBusinessLocation(etBusinessLocation.getText().toString());
        userData.setDecodableBusinessString(businessIconBitmapString);
        userData.setDecodableFaceString(faceBitmapString);
        userData.applyUpdate();

        Toast.makeText(this, "Information Successfully Saved!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG_1 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                faceBitmapString = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
                bitmapFace = new Utility().resizeBitmap(BitmapFactory
                        .decodeFile(faceBitmapString), 1920, 1080);
                ivFace.setImageBitmap(bitmapFace);

            } else if (requestCode == RESULT_LOAD_IMG_2 && resultCode == RESULT_OK
                    && null != data) {

                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                businessIconBitmapString = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
                bitmapBusinessIcon = new Utility().resizeBitmap(BitmapFactory
                        .decodeFile(businessIconBitmapString), 1920, 1080);

                ivBusinessIcon.setImageBitmap(bitmapBusinessIcon);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

}
