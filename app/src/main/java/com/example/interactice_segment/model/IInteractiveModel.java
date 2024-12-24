package com.example.interactice_segment.model;

import android.graphics.Bitmap;

import com.example.interactice_segment.model.tool.GetImageCallback;

public interface IInteractiveModel
{
    void uploadClick(float originalX, float originalY, int isPositive);


    void getImage(GetImageCallback callback);

    void finish();

    void undo();
}
