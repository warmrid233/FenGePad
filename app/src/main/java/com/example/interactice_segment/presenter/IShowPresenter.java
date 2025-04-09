package com.example.interactice_segment.presenter;

import android.graphics.Bitmap;

import com.example.interactice_segment.model.tool.UploadImgCallback;

public interface IShowPresenter extends IBasePresenter
{
    void uploadImg(Bitmap bitmap, UploadImgCallback callback);
    void saveBitmap(Bitmap bitmap, String title);
}
