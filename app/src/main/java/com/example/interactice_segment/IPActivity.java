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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        if(actionId == EditorInfo.IME_ACTION_DONE)
        {
            ip_port = ip_port_bar.getText().toString();
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
                    showMessage("未连接到模型，无法开始");
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
        showMessage("模型连接失败");
    }
}
