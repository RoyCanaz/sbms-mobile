package org.totalit.sbms.fragments;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.totalit.sbms.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static android.app.Activity.RESULT_OK;

public class FragmentTakePicture extends Fragment {

    private Button btnTakePicture, btnOpenPictures;
    private Button startButton, stopButton;

    String filePath, filename;
    Bitmap yourSelectedImage;
    ImageView image;
    Uri fileUri;
    String photoPath = "";
    String SCAN_PATH;
    File[] allFiles ;
    public static final int ACTIVITY_RECORD_SOUND = 111;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capture_photo, container, false);

        hasSDCard();
        checkCameraHardware(getActivity().getApplicationContext());
        btnTakePicture = view.findViewById(R.id.btn_take_photo);
        btnOpenPictures = view.findViewById(R.id.btn_open_photos);
        startButton = view.findViewById(R.id.btn_start);
        image = view.findViewById(R.id.image);
        File folder = new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures");
        //File folder = new File(getPath(fileUri));
        allFiles = folder.listFiles();
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnOpenPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SingleMediaScanner(getActivity().getApplicationContext(), allFiles[0]);
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecoder();
            }
        });
        return view;
    }
    private void openCamera(){

        String fileName = "sbms_image"+System.currentTimeMillis()+".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        fileUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, 0);
    }
    private void openRecoder(){

        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, ACTIVITY_RECORD_SOUND);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            try
            {
                photoPath = getPath(fileUri);

                Toast.makeText(getActivity().getApplicationContext(), photoPath, Toast.LENGTH_LONG).show();

                Bitmap b = decodeUri(fileUri);
                image.setImageBitmap(b);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException, FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();

        o.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(getActivity().getContentResolver()
                .openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 72;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;

        int scale = 1;

        while (true)
        {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
            {
                break;
            }
            width_tmp /= 2;

            height_tmp /= 2;

            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();

        o2.inSampleSize = scale;

        Bitmap bitmap;
        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver()
                .openInputStream(selectedImage), null, o2);

        return bitmap;
    }
    @SuppressWarnings("deprecation")
    private String getPath(Uri selectedImaeUri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
      //  Cursor cursor = getActivity().managedQuery(selectedImaeUri, projection, null, null, null);
        Cursor cursor = getActivity().getContentResolver().query(selectedImaeUri, projection, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            return cursor.getString(columnIndex);
        }

        return selectedImaeUri.getPath();
    }

    private void hasSDCard(){
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(isSDPresent)        {
            Toast.makeText(getActivity().getApplicationContext(), "SD Card Detected", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "SD Not Detected", Toast.LENGTH_LONG).show();
        }
    }
    private void checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(getActivity().getApplicationContext(), "Camera Detected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Camera Not Detected", Toast.LENGTH_LONG).show();
        }
    }

    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        public void onScanCompleted(String path, Uri uri) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
            mMs.disconnect();
        }

    }
}
