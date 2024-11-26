package com.example.interactice_segment.presenter;

import android.app.Activity;
import android.graphics.Bitmap;

public interface IShowPresenter extends IBasePresenter
{
    void uploadImg(Activity context, Bitmap bitmap);
    void saveBitmap(Bitmap bitmap, String title);
}
