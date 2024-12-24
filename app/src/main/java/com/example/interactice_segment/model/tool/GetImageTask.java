package com.example.interactice_segment.model.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetImageTask extends AsyncTask<Void, Void, Bitmap>
{
    private GetImageCallback callback;

    // 构造函数传入回调接口
    public GetImageTask(GetImageCallback callback) {
        this.callback = callback;
    }


    @Override
    protected Bitmap doInBackground(Void... urls) {
        Bitmap bitmap = null;
        try {
            // 创建连接
            URL url = new URL("http://10.129.234.121:5000/get_image");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            // 获取输入流并解析成 Bitmap
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            Log.e("GetImageTask", "Error downloading image: " + e.getMessage());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            // 更新UI
            callback.onImageGot(result);
        } else {
            callback.onGetFailed();
        }
    }
}
