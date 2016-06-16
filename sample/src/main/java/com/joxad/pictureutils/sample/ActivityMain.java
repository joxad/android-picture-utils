package com.joxad.pictureutils.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.joxad.pictureutils.PictureUtils;
/**
 * Created by josh on 16/06/16.
 */
public class ActivityMain extends AppCompatActivity {

    private static final String TAG = ActivityMain.class.getSimpleName();
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.iv_picture);
        new PictureUtils.Builder().context(this).build();
        PictureUtils.setListener(new PictureUtils.Listener() {
            @Override
            public void onImageSelected(Uri fileUri) {
                imageView.setImageURI(fileUri);
            }
        });

    }

    public void onClick(View view) {
        PictureUtils.askPermission(new PictureUtils.IPermission() {
            @Override
            public void onPermissionAccepted() {
                PictureUtils.showDialogPicker("Title", "Take Photo", "From Gallery", "Cancel");
            }

            @Override
            public void onPermissionDenied() {

                Log.d(TAG, "Denied permission");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PictureUtils.onRequestPermissionsResults(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PictureUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
