package com.example.interactice_segment.model.tool;

import android.graphics.Bitmap;

public interface TestCallback
{
    void onTestSuccess(String result); // 成功时回调
    void onTestFailed(); // 失败时回调
}
