package com.example.interactice_segment.presenter;

import android.graphics.Bitmap;

import com.example.interactice_segment.model.IInteractiveModel;
import com.example.interactice_segment.model.InteractiveModel;
import com.example.interactice_segment.model.tool.GetImageCallback;
import com.example.interactice_segment.view.BaseView;

public class InteractivePresenter extends BasePresenter implements IInteractivePresenter
{
    private IInteractiveModel model;

    public InteractivePresenter(BaseView view)
    {
        super(view);
        model = new InteractiveModel();
    }

    @Override
    public void uploadClick(float originalX, float originalY, int isPositive)
    {
        model.uploadClick(originalX, originalY, isPositive);
    }

    @Override
    public void getImage(GetImageCallback callback) {
        model.getImage(callback);
    }


}
