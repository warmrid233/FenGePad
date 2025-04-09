package com.example.interactice_segment.model.tool;

import android.graphics.Bitmap;

// GetImageTask类的回调接口
public interface GetImageCallback
{
    void onImageGot(Bitmap bitmap); // 成功时回调
    void onGetFailed(); // 失败时回调
}
