package com.example.interactice_segment.presenter;

import com.example.interactice_segment.model.tool.GetImageCallback;

public interface IInteractivePresenter extends IBasePresenter
{
    void uploadClick(float originalX, float originalY, int isPositive);

    void getImage(GetImageCallback callback);

    void finish();

    void undo();
}
