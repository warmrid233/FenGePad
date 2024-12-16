package com.example.interactice_segment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.interactice_segment.model.tool.GetImageCallback;
import com.example.interactice_segment.view.DrawingView;
import com.example.interactice_segment.view.ZoomableImageView;
import com.example.interactice_segment.presenter.IInteractivePresenter;
import com.example.interactice_segment.presenter.InteractivePresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InteractiveActivity extends BaseActivity implements GetImageCallback
{
    private Bitmap bitmap;
    private DrawingView drawingView;
    private ZoomableImageView imageView;
    private FrameLayout frameLayout;

    private IInteractivePresenter presenter;

    private int method = 0; //0 - 图片缩放、移动， 1 - 点击， 2 - 连线， 3 - 画笔
    private int is_positive = 1;
    private int resetImg = 1;
    @Override
    protected int getLayoutId()
    {
        return R.layout.activity_interactive;
    }

    @SuppressLint("ClickableViewAccessibility")
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
        imageView = findViewById(R.id.img_show);
        presenter = new InteractivePresenter(this);
        drawingView = findViewById(R.id.drawing_view);

        Intent intent = this.getIntent();
        String imagePath = intent.getStringExtra("imagePath");
        if (imagePath != null)
        {
            File imgFile = new  File(imagePath);
            if (imgFile.exists())
            {
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            }
        }
        // 调整画布与图片大小一致
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // 设置 DrawingView 的宽高与 Bitmap 的宽高一致
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) drawingView.getLayoutParams();
        params.width = bitmapWidth;
        params.height = bitmapHeight;
        params.gravity = Gravity.CENTER;

        drawingView.setLayoutParams(params);

        // 设置 ZoomableImageView 的宽高与 Bitmap 的宽高一致
        params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.width = bitmapWidth;
        params.height = bitmapHeight;
        params.gravity = Gravity.CENTER; // 居中显示

        imageView.setLayoutParams(params);

        final int RED_CONST = 1;
        final int BLUE_CONST = 2;


        btn_moveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method = 0;
                drawingView.setMethod(method);
                if(resetImg == 0) resetImg = 1;
                else
                {
                    imageView.reset();
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
                resetImg = 0;
                if (method != 1) drawingView.clear();

                frameLayout.bringChildToFront(drawingView);
                frameLayout.invalidate();

                //点击兴趣点的实现
                method = 1;
                is_positive = 1;
                drawingView.setMethod(method);
                drawingView.setColor(RED_CONST);
            }
        });

        btn_uninterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImg = 0;
                if (method != 1) drawingView.clear();

                frameLayout.bringChildToFront(drawingView);
                frameLayout.invalidate();
                //点击非兴趣点的实现
                method = 1;
                is_positive = 0;
                drawingView.setMethod(1);
                drawingView.setColor(BLUE_CONST);
            }
        });

        btn_lines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImg = 0;
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
                resetImg = 0;
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
                resetImg = 0;
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
                File file = new File(getCacheDir(), "temp_image.jpg");
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent();
                intent.setClass(InteractiveActivity.this, ShowActivity.class);
                intent.putExtra("imagePath", file.getAbsolutePath());

                startActivity(intent);
                drawingView.clear();
                finish();
            }
        });

        drawingView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(method == 1)
                    {
                        float x = event.getX();
                        float y = event.getY();
                        // 计算原始图像的坐标
                        PointF originalCoords = imageView.getOriginalImageCoordinates(x, y);
                        float originalX = originalCoords.x;
                        float originalY = originalCoords.y;
                        Log.d("Click Position", "Original X: " + originalX + ", " +
                                "Original Y: " + originalY);

                        presenter.uploadClick(originalX, originalY, is_positive);

                        presenter.getImage(InteractiveActivity.this);
                    }
                }
                return false;   //返回false使得触摸事件在drawingView的OnTouchEvent中再次被处理
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

    @Override
    public void onImageGot(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onGetFailed() {
        showMessage("图片更新失败");
    }
}
