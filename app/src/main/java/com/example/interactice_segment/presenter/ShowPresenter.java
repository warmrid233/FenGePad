package com.example.interactice_segment.presenter;

import android.app.Activity;
import android.graphics.Bitmap;

import com.example.interactice_segment.ShowActivity;
import com.example.interactice_segment.model.IShowModel;
import com.example.interactice_segment.model.ShowModel;
import com.example.interactice_segment.view.BaseView;

public class ShowPresenter extends BasePresenter implements IShowPresenter
{
    private final IShowModel model;

    public ShowPresenter(BaseView view)
    {
        super(view);
        model = new ShowModel();
    }

    @Override
    public void uploadImg(Bitmap bitmap)
    {
        model.uploadImage(bitmap);
    }

    @Override
    public void saveBitmap(Bitmap bitmap, String title)
    {
        if (view != null && bitmap != null) {
            model.saveBitmapToGallery((ShowActivity) view, bitmap, title);
            view.showMessage("图片保存成功");
        } else {
            if (view != null) {
                view.showMessage("图片保存失败");
            }
        }
    }
}
