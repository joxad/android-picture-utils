package com.joxad.pictureutils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * {@link PictureUtils} is an helper to take picture and write it to the external storage handling the Marshmallow permissions
 */
public class PictureUtils {
    static String mCurrentPhotoPath;


    private static final int PERMISSION_WRITE_STORAGE = 1337;
    private static final String TAG = PictureUtils.class.getSimpleName();
    private static Uri fileUri; // file url to store image
    // Activity request codes
    private static final int RQ_TAKE = 100;
    private static final int RQ_SELECT = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "pictures";
    private static Activity activity;

    private static Listener listener;
    private static IPermission iPermission;
    private static FragmentManager fragmentManager;

    /***
     * Init with the activity needed
     *
     * @param activity
     * @param fragmentManager
     */
    private static void init(Activity activity, FragmentManager fragmentManager) {
        PictureUtils.activity = activity;
        PictureUtils.fragmentManager = fragmentManager;

    }
    /**
     * ------------ Helper Methods ----------------------
     * */



    /***
     *
     */
    public static void showDialogPicker(String title, String takePhotoOption, String pickPhotoOption, String cancel) {

        PictureDialogChooserFragment bottomSheetDialogFragment = PictureDialogChooserFragment.newInstance(title);
        bottomSheetDialogFragment.show(fragmentManager, bottomSheetDialogFragment.getTag());

       /* final CharSequence[] items = {takePhotoOption, pickPhotoOption, cancel};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(takePhotoOption)) {
                takePhoto();
            } else if (items[item].equals(pickPhotoOption)) {
                pickPhoto();
            } else if (items[item].equals(cancel)) {
                dialog.dismiss();
            }
        });
        builder.show();*/
    }

    private static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    /****
     * Call the
     */
    protected static void takePhoto() {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photoFile==null)return;
        fileUri = FileProvider.getUriForFile(activity,
                activity.getPackageName() + ".provider",
                photoFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);


        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        activity.startActivityForResult(intent, RQ_TAKE);
    }

    /***
     *
     */
    protected static void pickPhoto() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, RQ_SELECT);
    }

    /***
     * Handle the actvity result to get the image
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_CANCELED)
            return;
        switch (requestCode) {
            case RQ_TAKE:
                if (resultCode == Activity.RESULT_OK) {
                    listener.onImageSelected(fileUri);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(activity,
                            "User cancelled image capture", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(activity,
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case RQ_SELECT:
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();
                Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                cursor.close();
                listener.onImageSelected(Uri.parse(picturePath));
                break;
        }
    }

    /***
     *
     */
    public static void askPermission(IPermission iPermission) {
        PictureUtils.iPermission = iPermission;
        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            iPermission.onPermissionAccepted();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_STORAGE);
        }
    }

    /***
     * Handle the permission - See Marshmallow
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResults(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    iPermission.onPermissionAccepted();
                } else {
                    iPermission.onPermissionDenied();
                }
                return;
            }
        }
    }

    public static void setListener(Listener listener) {
        PictureUtils.listener = listener;
    }


    /***
     * Interface that give the uri of the file created when the user selected the image
     */
    public interface Listener {
        void onImageSelected(Uri fileUri);
    }

    /***
     * Interface to handle the permission storage
     */
    public interface IPermission {
        void onPermissionAccepted();

        void onPermissionDenied();
    }

    /***
     * Builder used to create our GCM Manager
     */
    public static class Builder {

        private Activity activity;
        private FragmentManager fragmentManager;


        /**
         * @throws RuntimeException if Context has not been set.
         */
        public void build() {
            PictureUtils.init(activity, fragmentManager);
        }

        public Builder supportFragmentManager(FragmentManager supportFragmentManager) {
            fragmentManager = supportFragmentManager;
            return this;
        }


        /**
         * Set the Context used to instantiate the EasyGcm
         *
         * @param context the application context
         */
        public Builder context(@NonNull final Activity context) {
            activity = context;
            return this;
        }
    }

}
