package com.example.interactice_segment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.interactice_segment.bean.DrawingView;
import com.example.interactice_segment.bean.ZoomableImageView;
import com.example.interactice_segment.presenter.IInteractivePresenter;
import com.example.interactice_segment.presenter.InteractivePresenter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class InteractiveActivity extends BaseActivity
{
    private Bitmap bitmap;
    private DrawingView drawingView;
    private FrameLayout frameLayout;

    private IInteractivePresenter presenter;
    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_interactive;
    }

    @Override
    protected void initViews()
    {
        Button btn_moveImg = findViewById(R.id.top_move_img);
        Button btn_interested = findViewById(R.id.top_click_interested);
        Button btn_uninterested = findViewById(R.id.top_click_uninterested);
        Button btn_lines = findViewById(R.id.top_line);
        Button btn_pen = findViewById(R.id.top_pen);
        Button btn_rollback = findViewById(R.id.top_rollback);
        Button btn_exit = findViewById(R.id.top_exit);

        frameLayout = findViewById(R.id.frame);
        ZoomableImageView imageView = findViewById(R.id.img_show);
        presenter = new InteractivePresenter(this);
        drawingView = findViewById(R.id.drawing_view);

        Intent intent = this.getIntent();
        byte[] byteTemp = intent.getByteArrayExtra("bp");
        if (byteTemp != null)
        {
            bitmap = BitmapFactory.decodeByteArray(byteTemp, 0, byteTemp.length);
            imageView.setImageBitmap(bitmap);
        }

        final int RED_CONST = 1;
        final int BLUE_CONST = 2;

        btn_moveImg.setOnClickListener(new View.OnClickListener() {
            int resetImg = 0;
            @Override
            public void onClick(View v) {
                drawingView.setMethod(0);
                if(resetImg == 0) resetImg = 1;
                else
                {
                    imageView.reset();
                    resetImg = 0;
                    showMessage("图片已复位");
                }
                // 改变视图的堆叠顺序
                frameLayout.bringChildToFront(imageView);
                frameLayout.invalidate();
            }
        });

        btn_interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();
                frameLayout.bringChildToFront(drawingView);
                frameLayout.invalidate();

                //点击兴趣点的实现
                drawingView.setMethod(1);
                drawingView.setColor(RED_CONST);
            }
        });

        btn_uninterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();
                frameLayout.bringChildToFront(drawingView);
                frameLayout.invalidate();
                //点击非兴趣点的实现
                drawingView.setMethod(1);
                drawingView.setColor(BLUE_CONST);
            }
        });

        btn_lines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();
                frameLayout.bringChildToFront(drawingView);
                frameLayout.invalidate();
                //画笔的实现
                drawingView.setMethod(2);
                drawingView.setColor(RED_CONST);
            }
        });

        btn_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clear();
                frameLayout.bringChildToFront(drawingView);
                frameLayout.invalidate();
                //画笔的实现
                drawingView.setMethod(3);
                drawingView.setColor(RED_CONST);
            }
        });

        btn_rollback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(frameLayout.indexOfChild(imageView) > frameLayout.indexOfChild(drawingView))
                {
                    frameLayout.bringChildToFront(drawingView);
                    frameLayout.invalidate();
                }
                //撤回上一步的实现
                drawingView.setMethod(0);
                drawingView.undo();
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将交互的结果返回给showActivity，然后销毁本Activity
                Intent intent = new Intent();
                intent.setClass(InteractiveActivity.this, ShowActivity.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bs);
                byte[] bitmapByte = bs.toByteArray();
                intent.putExtra("bp", bitmapByte);

                startActivity(intent);
                drawingView.clear();
                finish();
            }
        });

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(presenter != null)
        {
            presenter.detachView();
        }
    }
}
