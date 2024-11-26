package com.example.interactice_segment.presenter;

import com.example.interactice_segment.model.IInteractiveModel;
import com.example.interactice_segment.model.InteractiveModel;
import com.example.interactice_segment.view.BaseView;

public class InteractivePresenter extends BasePresenter implements IInteractivePresenter
{
    private IInteractiveModel model;

    public InteractivePresenter(BaseView view)
    {
        super(view);
        model = new InteractiveModel();
    }



}
