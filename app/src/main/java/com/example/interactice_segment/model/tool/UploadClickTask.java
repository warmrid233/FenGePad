package com.example.interactice_segment.model.tool;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UploadClickTask extends AsyncTask<Float, Void, String> implements BaseTask
{
    private String ip_port = null;

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

            String url_s = "http://" + this.ip_port + "/click";
            // 设置Flask服务器的URL
            URL url = new URL(url_s);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);

            // 创建请求体
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("x", x);
            jsonParam.put("y", y);
            jsonParam.put("flag", is_positive);

            List<Float> point_coords = new ArrayList<Float>();
            point_coords.add(x);
            point_coords.add(y);
            jsonParam.put("point_coords",point_coords);
            jsonParam.put("point_lables", is_positive);

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

    @Override
    public void setIp_port(String param) {
        this.ip_port = param;
    }
}
