package com.example.interactice_segment.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.interactice_segment.model.tool.UploadImgCallback;

public interface IShowModel
{
    public void saveBitmapToGallery(Context context, Bitmap bitmap, String title);
    public void uploadImage(Bitmap bitmap, UploadImgCallback callback);
}
