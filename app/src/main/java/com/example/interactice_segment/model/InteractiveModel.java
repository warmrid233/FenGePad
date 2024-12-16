package com.example.interactice_segment.model;

import android.graphics.Bitmap;

import com.example.interactice_segment.model.tool.GetImageCallback;
import com.example.interactice_segment.model.tool.GetImageTask;
import com.example.interactice_segment.model.tool.UploadClickTask;

public class InteractiveModel implements IInteractiveModel
{
    @Override
    public void uploadClick(float originalX, float originalY, int isPositive)
    {
        new UploadClickTask().execute(originalX, originalY, (float) isPositive);
    }

    @Override
    public void getImage(GetImageCallback callback) {
        new GetImageTask(callback).execute();
    }

}
