package com.example.interactice_segment.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.interactice_segment.model.tool.UploadBitmapTask;
import com.example.interactice_segment.model.tool.UploadImgCallback;

import java.io.IOException;
import java.io.OutputStream;

public class ShowModel implements IShowModel
{
    private String ip_port = null;

    public ShowModel(String ip_port)
    {
        this.ip_port = ip_port;
    }

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
    public void uploadImage(Bitmap bitmap, UploadImgCallback callback)
    {
        UploadBitmapTask ubt = new UploadBitmapTask(callback);
        ubt.setIp_port(ip_port);
        ubt.execute(bitmap);
    }
}
