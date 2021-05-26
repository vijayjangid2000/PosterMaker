package com.vijay.postermaker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Utility util;
    Button btn_CreatePoster, btn_sharePoster, btn_setDetails;
    Poster poster;
    ProgressBar progressPoster;
    ImageView iv_poster;
    int REQUEST_CODE = 100;
    Uri imageUri;
    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_CreatePoster = findViewById(R.id.btn_createPoster);
        btn_sharePoster = findViewById(R.id.btn_sharePoster);
        progressPoster = findViewById(R.id.progressBar);
        iv_poster = findViewById(R.id.imageView);
        btn_setDetails = findViewById(R.id.btn_setDetails);
        util = Utility.getInstance();

        btn_setDetails.setOnClickListener(view -> {
            if (checkLocationPermission())
                startActivity(new Intent(MainActivity.this, UserFormActy.class));
            else toast("Please give Storage permission");
        });

        btn_CreatePoster.setOnClickListener(view -> {
            if (checkLocationPermission()) {
                toast("Creating Poster Please wait");
                createPoster();
            } else toast("Please give Storage permission");
        });

        btn_sharePoster.setOnClickListener(view -> {
            if (checkLocationPermission()) {
                if (imageUri != null) shareSavedBitmap();
                else toast("Create poster first");
            } else toast("Please give Storage permission");
        });

        iv_poster.setOnClickListener(view -> {
            toast("Opening image in Albums please wait");
            showBitmapInGallery(imageFile);
        });
    }

    private void createPoster() {

        UserData userData = UserData.getInstance(this);
        if (!userData.canCreatePoster()) {
            toast("We cannot find the poster and Face, Please set them first ");
            return;
        }

        progressPoster.setVisibility(View.VISIBLE);
        iv_poster.setVisibility(View.GONE);
        btn_sharePoster.setVisibility(View.GONE);
        btn_setDetails.setVisibility(View.GONE);
        btn_CreatePoster.setVisibility(View.GONE);

        try {

            Runnable runnable = () -> {
                poster = new Poster(context());
                if (poster.latestSavedBitmap != null)
                    saveBitmapToDownloads(poster.latestSavedBitmap);
                else toast("Please set face and poster first");
            };

            Thread thread = new Thread(runnable);
            thread.start();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (!thread.isAlive()) {
                        runOnUiThread(() -> {

                            if (poster.latestSavedBitmap != null) {
                                iv_poster.setImageBitmap(poster.latestSavedBitmap);
                                iv_poster.setVisibility(View.VISIBLE);
                            }

                            progressPoster.setVisibility(View.GONE);
                            btn_CreatePoster.setVisibility(View.VISIBLE);
                            btn_sharePoster.setVisibility(View.VISIBLE);
                            btn_setDetails.setVisibility(View.VISIBLE);

                        });

                        timer.cancel();
                    }
                }
            }, 0, 1000);

        } catch (Exception e) {
            toast("Error: " + " Please set face and poster in set details");
        }
    }

    private void shareSavedBitmap() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private void showBitmapInGallery(File file) {

        Uri mImageCaptureUri = FileProvider.getUriForFile(
                this,
                this.getApplicationContext()
                        .getPackageName() + ".provider", file);

        Intent view = new Intent();
        view.setAction(Intent.ACTION_VIEW);
        view.setData(mImageCaptureUri);
        List<ResolveInfo> resInfoList =
                this.getPackageManager()
                        .queryIntentActivities(view, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            this.grantUriPermission(packageName, mImageCaptureUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        view.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(mImageCaptureUri, "image/*");
        this.startActivity(intent);
    }

    private void saveBitmapToDownloads(Bitmap bitmap) {

        String message = "";
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Downloads");
        if (!dir.exists()) dir.mkdirs();
        File output = new File(dir, "createdPoster.png");

        try {
            OutputStream os = new FileOutputStream(output);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();

            //this code will scan the image so that it
            // will appear in your gallery when you open next time
            MediaScannerConnection.scanFile(this, new String[]{output.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.d("appname", "image is saved in gallery and gallery is refreshed.");
                            imageUri = uri;
                        }
                    }
            );

            message = "Image Saved in downloads: Downloads/" + output.getName();

        } catch (Exception e) {
            message = "Error in Saving Image\n" + e.getMessage();
            e.printStackTrace();
        }

        String finalMessage = message;
        runOnUiThread(() -> toast(finalMessage));

        imageFile = output;
    }

    private Context context() {
        return MainActivity.this;
    }

    private void toast(String message) {
        Toast.makeText(this,
                message, Toast.LENGTH_SHORT).show();
    }

    boolean checkLocationPermission() {
        boolean isStorageGranted = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        if (!isStorageGranted) toast("Please grant storage permission");

        return isStorageGranted;
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                toast("Granted Successfully! , Now you can create poster");
            } else {
                toast("Please grant Error: permission");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}