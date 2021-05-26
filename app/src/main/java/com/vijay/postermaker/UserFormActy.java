package com.vijay.postermaker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.theapache64.removebg.RemoveBg;
import com.theapache64.removebg.utils.ErrorResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.media.ExifInterface;

public class UserFormActy extends AppCompatActivity {

    final int RESULT_LOAD_FACE = 1;   // face
    final int RESULT_LOAD_POSTER = 2; // business

    TextInputLayout tilName, tilPostDetails, tilWhatsAppInfo, tilInstaInfo,
            tilTwitterInfo, tilFacebookInfo, tilWebsiteUrl,
            tilBusinessEmail, tilBusinessName, tilBusinessLocation;

    EditText etName, etPostDetails, etWhatsAppInfo, etInstaInfo,
            etTwitterInfo, etFacebookInfo, etWebsiteUrl,
            etBusinessEmail, etBusinessName, etBusinessLocation;

    Button btnSave, btn_changeFace, btn_changeBusinessIcon;

    UserData userData;
    ImageView ivFace, ivPoster;
    Bitmap bitmapFace, bitmapPoster;
    TextView fontExample;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        initViews();
    }

    void initViews() {
        userData = UserData.getInstance(this);

        fontExample = findViewById(R.id.tv_fontSample);
        spinner = findViewById(R.id.spinner);
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
        ivPoster = findViewById(R.id.iv_poster);
        btnSave = findViewById(R.id.btn_save);
        btn_changeBusinessIcon = findViewById(R.id.btn_changePoster);
        btn_changeFace = findViewById(R.id.btn_changeFaceImage);

        initListeners();
        setSavedInfoIntoViews();
    }

    void setSavedInfoIntoViews() {
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

        if (userData.getBitmapFace() != null) {
            bitmapFace = userData.getBitmapFace();
            ivFace.setImageBitmap(bitmapFace);
        } else {
            ivFace.setVisibility(View.GONE);
        }

        if (userData.getBitmapPoster() != null) {
            bitmapPoster = userData.getBitmapPoster();
            ivPoster.setImageBitmap(bitmapPoster);
        } else {
            btn_changeFace.setVisibility(View.GONE);
            ivPoster.setVisibility(View.GONE);
        }
    }

    void initListeners() {

        ArrayList<String> listFontName = new ArrayList<>();

        Map<String, Typeface> map = Utility.getInstance().getSystemFontList();
        for (Map.Entry<String, Typeface> entry : map.entrySet()) {
            listFontName.add(entry.getKey());
            Log.d("FontMap", entry.getKey() + " ---> " + entry.getValue() + "\n");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_text, listFontName);

        spinner.setAdapter(dataAdapter);
        if (listFontName.contains(userData.fontChosenKey)) {
            spinner.setSelection(listFontName.indexOf(userData.fontChosenKey));
            fontExample.setText("This is an example of " + userData.fontChosenKey);
            fontExample.setTypeface(Utility.getInstance().getSystemFontList().get(userData.fontChosenKey));
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userData.fontChosenKey = listFontName.get(i);
                TextView textView = (TextView) view;
                textView.setTypeface(Utility.getInstance().getSystemFontList().get(userData.fontChosenKey));
                fontExample.setTypeface((Utility.getInstance().getSystemFontList().get(userData.fontChosenKey)));
                fontExample.setText("This is an example of " + userData.fontChosenKey);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });

        btn_changeFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_FACE);
            }
        });

        btn_changeBusinessIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_POSTER);
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
        if (bitmapPoster != null) {
            userData.setPosterByteArrayString(bitmapPoster);
        }
        if (bitmapFace != null) {
            userData.setFaceByteArrayString(bitmapFace);
        }

        userData.applyUpdate(getApplicationContext());

        Toast.makeText(this,
                "SAVED Successfully!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_POSTER && resultCode == RESULT_OK
                    && null != data) {


                // Get the Image from data

                Uri selectedImageUri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String posterBitmapString = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
                bitmapPoster = Utility.getInstance().resizeBitmapNormal(BitmapFactory
                        .decodeFile(posterBitmapString));

                // now rotate bitmap
                int rotate = getCameraPhotoOrientation(
                        UserFormActy.this, selectedImageUri,
                        getRealPathFromURI(selectedImageUri));

                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);

                bitmapPoster = Bitmap.createBitmap(bitmapPoster
                        , 0, 0, bitmapPoster.getWidth(),
                        bitmapPoster.getHeight(), matrix, false);

                ivPoster.setImageBitmap(bitmapPoster);
                ivPoster.setVisibility(View.VISIBLE);

                btn_changeFace.setVisibility(View.VISIBLE);

            } else if (requestCode == RESULT_LOAD_FACE && resultCode == RESULT_OK
                    && null != data) {

                btn_changeFace.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);

                // Get the Image from data

                Uri selectedImageUri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                int rotate = getCameraPhotoOrientation(
                        UserFormActy.this, selectedImageUri,
                        getRealPathFromURI(selectedImageUri));

                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String faceBitmapString = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
                bitmapFace = Utility.getInstance().resizeBitmapNormal(BitmapFactory
                        .decodeFile(faceBitmapString));

                bitmapFace = Bitmap.createBitmap(bitmapFace
                        , 0, 0, bitmapFace.getWidth(),
                        bitmapFace.getHeight(), matrix, false);

                bitmapFace = getPerfectSize(bitmapFace, bitmapPoster);
                ivFace.setImageBitmap(this.bitmapFace);
                ivFace.setVisibility(View.VISIBLE);

                /*   Removing background here   */

                TextView textView = findViewById(R.id.tv_progress);
                textView.setVisibility(View.VISIBLE);

                File f3 = new File(Environment.getExternalStorageDirectory() + "/inpaint/");
                if (!f3.exists()) f3.mkdirs();
                OutputStream outStream;
                File file = new File(Environment.getExternalStorageDirectory() + "/inpaint/" + "temp" + ".png");

                try {
                    outStream = new FileOutputStream(file);
                    bitmapFace.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                RemoveBg.INSTANCE.init("2Skmd4d3YnFTyfBJCm7SGGoM");

                RemoveBg.INSTANCE.from(file, new RemoveBg.RemoveBgCallback() {

                    @Override
                    public void onUploadProgress(float v) {
                        runOnUiThread(() -> textView.setText
                                ("Removing Background: " + v));
                    }

                    @Override
                    public void onProcessing() {
                        runOnUiThread(() -> textView.setText("Processing..."));
                    }

                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        runOnUiThread(() -> {
                            bitmapFace = bitmap;
                            ivFace.setImageBitmap(bitmap);
                            textView.setText("Successfully Removed Background!");
                            btn_changeFace.setVisibility(View.VISIBLE);
                            btnSave.setVisibility(View.VISIBLE);
                        });
                    }

                    @Override
                    public void onError(List<ErrorResponse.Error> list) {
                        runOnUiThread(() -> {
                            textView.setText("Error: in removing background, Check Internet or API Free Exhausted");
                            btn_changeFace.setVisibility(View.VISIBLE);
                            btnSave.setVisibility(View.VISIBLE);
                        });
                    }

                });

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }

    }

    String getRealPathFromURI(Uri uri) {

        Cursor cursor = getContentResolver().query(uri,
                null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }

    int getCameraPhotoOrientation(
            Context context, Uri imageUri, String imagePath) {

        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    Bitmap getPerfectSize(Bitmap face, Bitmap posterBitmap) {

        /*  We are doing this to calculate the size of the
         * bitmap that needed to remove the background */

        PosterProperties posterProperties = PosterProperties.buildNewProperties(bitmapPoster);
        float ratio = posterProperties.faceSizeRatioToPoster;
        float hPoster, wPoster;

        hPoster = posterBitmap.getHeight();
        wPoster = posterBitmap.getWidth();

        Bitmap resized = Utility.getInstance().resizeBitmap(
                face, hPoster / ratio, wPoster / ratio);

        return resized;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAndExit();
    }

}
