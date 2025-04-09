package com.example.interactice_segment;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.interactice_segment.view.BaseView;

public abstract class BaseActivity extends AppCompatActivity implements BaseView
{
    private Toast currentToast = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initViews();
    }
    protected abstract int getLayoutId();
    protected abstract void initViews();

    @Override
    public void showMessage(String message)
    {
        if (currentToast != null) {
            currentToast.cancel();
        }
        currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        currentToast.show();
    }

}
