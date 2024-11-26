package com.example.interactice_segment.presenter;

import com.example.interactice_segment.view.BaseView;

public class BasePresenter implements IBasePresenter
{
    protected BaseView view;

    public BasePresenter(BaseView view)
    {
        this.view = view;
    }

    @Override
    public void detachView()
    {
        view = null;
    }
}
