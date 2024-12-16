package com.example.interactice_segment.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.example.interactice_segment.model.tool.UploadBitmapTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowModel implements IShowModel
{

    @Override
    public void saveBitmapToGallery(Context context, Bitmap bitmap, String title)
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Create a new ContentResolver and insert the image
        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream outputStream = null;
            if (uri != null) {
                outputStream = resolver.openOutputStream(uri);
            }
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            }
            if (outputStream != null) {
                outputStream.flush();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadImage(Bitmap bitmap)
    {
        new UploadBitmapTask().execute(bitmap);
    }
}
