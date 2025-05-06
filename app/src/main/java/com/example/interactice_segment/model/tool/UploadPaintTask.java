package com.example.interactice_segment.model.tool;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadPaintTask extends AsyncTask<Bitmap, Void, String> implements BaseTask
{
    private String ip_port = null;

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        String url_s = "http://" + this.ip_port + "/paint";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            Bitmap bitmap = bitmaps[0];

            // 将 Bitmap 转换为 JPEG 格式的字节数组
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            // 打开连接
            URL url = new URL(url_s);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=*****");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);

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
            Log.e("UploadPaintTask", "Error uploading paint", e);
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

//    @Override
//    protected void onPostExecute(String result) {
//        super.onPostExecute(result);
//        JSONObject jsonResponse;
//        try {
//            jsonResponse = new JSONObject(result);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//
//        String message;
//        try {
//            message = jsonResponse.getString("message");
//            Log.d("UploadPaintTask", message);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void setIp_port(String param) {
        this.ip_port = param;
    }
}
