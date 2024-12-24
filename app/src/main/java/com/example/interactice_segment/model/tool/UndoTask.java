package com.example.interactice_segment.model.tool;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UndoTask extends AsyncTask<Void, Void, String>
{
    @Override
    protected String doInBackground(Void... voids)
    {
        try {
            // 创建连接
            URL url = new URL("http://10.129.234.121:5000/undo");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            // 获取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                byte[] response = new byte[inputStream.available()];
                inputStream.read(response);
                return new String(response);
            } else {
                return "Error: " + responseCode;
            }
        } catch (Exception e) {
            Log.e("UndoTask", "Error downloading image: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // 简单输出服务器返回的结果
        Log.d("UndoTask", result);
    }

}
