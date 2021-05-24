package com.vijay.postermaker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    Utility util;
    Button btn_CreatePoster, btn_sharePoster, btn_Details;
    Poster poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Details = findViewById(R.id.btn_setDetails);
        btn_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserForm.class));
            }
        });

        btn_CreatePoster = findViewById(R.id.btn_createPoster);
        btn_CreatePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                toast("Creating Poster please wait");
                createPoster();
            }
        });

        btn_sharePoster = findViewById(R.id.btn_sharePoster);
        btn_sharePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(poster.latestSavedBitmap != null) saveBitmap(poster.latestSavedBitmap);
                else toast("Create Poster then Share");
            }
        });

        //saveBitmap(addFace());
    }


    void createPoster() {
        try {

            util = new Utility();
            UserData userData = new UserData(this);

            poster = new Poster(this, userData.getBitmapBusIcon()
                    , userData.getBitmapFace());

            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(poster.getAttachedName());

        } catch (Exception e) {
            Toast.makeText(this, "Error: Please set details, " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        btn_CreatePoster.setEnabled(true);
    }

    private Context context() {
        return MainActivity.this;
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveBitmap(Bitmap b) {

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Downloads");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File output = new File(dir, "tempfile.jpg");
        OutputStream os = null;

        try {
            os = new FileOutputStream(output);
            b.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();

            //this code will scan the image so that it will appear in your gallery when you open next time
            MediaScannerConnection.scanFile(this, new String[]{output.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.d("appname", "image is saved in gallery and gallery is refreshed.");

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/png");
                            intent.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(Intent.createChooser(intent, "Share"));
                        }
                    }
            );
            toast("Image Saved");
        } catch (Exception e) {
            toast("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}