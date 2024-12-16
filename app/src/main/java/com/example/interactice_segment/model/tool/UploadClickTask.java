package com.example.interactice_segment.model.tool;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UploadClickTask extends AsyncTask<Float, Void, String> {
    @Override
    protected String doInBackground(Float... params)
    {
        String result = "";
        OutputStream os = null;
        BufferedReader in = null;
        HttpURLConnection urlConnection = null;
        try {
            float x = params[0];
            float y = params[1];
            int is_positive = (int)params[2].floatValue();

            // 设置Flask服务器的URL
            URL url = new URL("http://10.129.234.121:5000/click");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            // 创建请求体
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("x", x);
            jsonParam.put("y", y);
            jsonParam.put("flag", is_positive);

            // 发送请求体
            os = urlConnection.getOutputStream();
            byte[] input = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            // 获取响应
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            result = response.toString();

        } catch (Exception e) {
            Log.e("UploadClickTask", "Error during HTTP request", e);
            result = "Exception: " + e.getMessage();
        } finally {
            try {
                if (os != null) os.close();
                if (in != null) in.close();
            } catch (Exception e) {
                Log.e("UploadClickTask", "Error closing resources", e);
            }
            if(urlConnection != null) urlConnection.disconnect();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String message;
        try {
            message = jsonResponse.getString("message");
            Log.d("UploadClickTask", message);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
