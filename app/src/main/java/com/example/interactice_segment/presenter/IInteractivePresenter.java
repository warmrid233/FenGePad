package com.example.interactice_segment.presenter;

import android.graphics.Bitmap;

import com.example.interactice_segment.model.tool.GetImageCallback;

public interface IInteractivePresenter extends IBasePresenter
{
    void uploadClick(float originalX, float originalY, int isPositive);

    void getImage(GetImageCallback callback);

    void finish();

    void undo();

    void uploadPaintResult(Bitmap paintBitmap);

    void switch_label();
}
