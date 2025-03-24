package com.example.interactice_segment.presenter;

import android.graphics.Bitmap;

import com.example.interactice_segment.model.IInteractiveModel;
import com.example.interactice_segment.model.InteractiveModel;
import com.example.interactice_segment.model.tool.GetImageCallback;
import com.example.interactice_segment.view.BaseView;

public class InteractivePresenter extends BasePresenter implements IInteractivePresenter
{
    private String ip_port = null;
    private IInteractiveModel model;

    public InteractivePresenter(BaseView view, String ip_port)
    {
        super(view);
        model = new InteractiveModel(ip_port);
        this.ip_port = ip_port;
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

    @Override
    public void finish()
    {
        model.finish();
    }

    @Override
    public void undo() {
        model.undo();
    }


}
