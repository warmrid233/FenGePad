package com.example.interactice_segment.presenter;

import com.example.interactice_segment.view.BaseView;

public class BasePresenter implements IBasePresenter
{
    protected BaseView view;

    // 绑定View视图类
    public BasePresenter(BaseView view)
    {
        this.view = view;
    }

    // 解绑View视图类
    @Override
    public void detachView()
    {
        view = null;
    }
}
