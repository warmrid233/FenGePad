package com.example.interactice_segment.model.tool;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestTask extends AsyncTask<Void, Void, String> implements BaseTask
{
    private String ip_port = null;

    private TestCallback callback;

    public TestTask(TestCallback callback)
    {
        this.callback = callback;
    }


    @Override
    protected String doInBackground(Void... voids)
    {
        try {
            String url_s = "http://" + this.ip_port + "/test";
            // 创建连接
            URL url = new URL(url_s);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            // 获取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                // 读取响应内容
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getString("message");
            } else {
                return "Error: " + responseCode;
            }

        } catch (IOException e) {
            Log.e("UploadBitmapTask", "Error uploading bitmap", e);
            return "Error: " + e.getMessage();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (!result.startsWith("Error")) {
            // 更新UI
            callback.onTestSuccess(result);
        } else {
            callback.onTestFailed();
        }
    }

    @Override
    public void setIp_port(String param) {
        this.ip_port = param;
    }
}
