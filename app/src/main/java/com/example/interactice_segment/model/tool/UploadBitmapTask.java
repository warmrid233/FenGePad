package com.example.interactice_segment.model.tool;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadBitmapTask extends AsyncTask<Bitmap, Void, String>
{
    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        String urlString = "http://10.129.234.121:5000/load_img";  // 替换为你的 Flask 服务器地址
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            Bitmap bitmap = bitmaps[0];

            // 将 Bitmap 转换为 JPEG 格式的字节数组
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            // 打开连接
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=*****");

            // 创建输出流
            outputStream = new DataOutputStream(connection.getOutputStream());

            // 写入表单数据（图片文件）
            outputStream.writeBytes("--*****\r\n");
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"tempImg.jpg\"\r\n");
            outputStream.writeBytes("Content-Type: image/jpeg\r\n\r\n");

            // 使用流逐块写入字节数据
            int chunkSize = 4096;  // 设定每次上传的块大小
            int totalBytes = byteArray.length;
            int bytesWritten = 0;
            while (bytesWritten < totalBytes) {
                int remaining = totalBytes - bytesWritten;
                int bytesToWrite = Math.min(chunkSize, remaining);
                outputStream.write(byteArray, bytesWritten, bytesToWrite);
                bytesWritten += bytesToWrite;
            }

            outputStream.writeBytes("\r\n--*****--\r\n");


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

        } catch (IOException e) {
            Log.e("UploadBitmapTask", "Error uploading bitmap", e);
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null) connection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // 简单输出服务器返回的结果
        Log.d("UploadBitmapTask", result);
    }
}
