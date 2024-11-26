package com.example.interactice_segment.model;

import android.content.Context;
import android.graphics.Bitmap;

public interface IShowModel
{
    public void saveBitmapToGallery(Context context, Bitmap bitmap, String title);
    public int uploadImage(Bitmap bitmap);
}
