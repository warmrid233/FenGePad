package com.example.interactice_segment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.interactice_segment.model.tool.TestCallback;
import com.example.interactice_segment.model.tool.TestTask;

public class IPActivity extends BaseActivity implements TestCallback, TextView.OnEditorActionListener
{
    private String ip_port = null;
    private boolean is_connected = false;
    private Button btn_test;
    private Button btn_set;
    private EditText ip_port_bar;

    private static class AddressValidator
    {
        public boolean isValidFormat(String input) {
            if (input == null || input.isEmpty()) {
                return false;
            }

            String[] parts = input.split(":");
            if (parts.length != 2) {
                return false; // 无冒号或多余冒号
            }

            return isValidIPv4(parts[0]) && isValidPort(parts[1]);
        }

        private boolean isValidIPv4(String ip) {
            String[] octets = ip.split("\\.");
            if (octets.length != 4) {
                return false; // 不是四部分
            }

            for (String octet : octets) {
                // 空或长度超过3
                if (octet.isEmpty() || octet.length() > 3) {
                    return false;
                }
                // 前导零检查
                if (octet.charAt(0) == '0' && octet.length() > 1) {
                    return false;
                }
                try {
                    int num = Integer.parseInt(octet);
                    if (num < 0 || num > 255) {
                        return false; // 数字范围无效
                    }
                } catch (NumberFormatException e) {
                    return false; // 非数字
                }
            }
            return true;
        }

        private boolean isValidPort(String portStr) {
            try {
                int port = Integer.parseInt(portStr);
                return port >= 0 && port <= 65535;
            } catch (NumberFormatException e) {
                return false; // 非数字
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if(actionId == EditorInfo.IME_ACTION_DONE)
        {
            ip_port = ip_port_bar.getText().toString();
            if(new AddressValidator().isValidFormat(ip_port))
            {
//                showMessage("已成功设置服务器地址");
                showMessage("Successfully set address.");
            }
            else
            {
                ip_port = null;
//                showMessage("当前输入不合法，请按照提示样例输入地址");
                showMessage("Invalid address! Please check again.");
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ip;
    }

    @Override
    protected void initViews()
    {
        btn_test = findViewById(R.id.btn_test);
        btn_set = findViewById(R.id.btn_set);
        ip_port_bar = findViewById(R.id.ip_port);

        ip_port_bar.setOnEditorActionListener(this);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestTask tt = new TestTask(IPActivity.this);
                tt.setIp_port(ip_port);
                tt.execute();
            }
        });

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!is_connected)
                {
//                    showMessage("未连接到模型，无法开始");
                    showMessage("No model connected, not available.");
                }
                else
                {
                    Intent intent = new Intent();
                    intent.setClass(IPActivity.this, ShowActivity.class);
                    intent.putExtra("IpAndPort", ip_port);

                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onTestSuccess(String result)
    {
        is_connected = true;
        showMessage(result);
    }

    @Override
    public void onTestFailed()
    {
        is_connected = false;
//        showMessage("模型连接失败");
        showMessage("Connection failed.");
    }
}
