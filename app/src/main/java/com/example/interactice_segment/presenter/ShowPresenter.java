package com.example.interactice_segment.presenter;

import android.graphics.Bitmap;

import com.example.interactice_segment.ShowActivity;
import com.example.interactice_segment.model.IShowModel;
import com.example.interactice_segment.model.ShowModel;
import com.example.interactice_segment.model.tool.UploadImgCallback;
import com.example.interactice_segment.view.BaseView;

public class ShowPresenter extends BasePresenter implements IShowPresenter
{
    private String ip_port = null;
    private final IShowModel model;

    public ShowPresenter(BaseView view, String ip_port)
    {
        super(view);
        model = new ShowModel(ip_port);
        this.ip_port = ip_port;
    }

    @Override
    public void uploadImg(Bitmap bitmap, UploadImgCallback callback)
    {
        model.uploadImage(bitmap, callback);
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
