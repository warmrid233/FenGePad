package com.example.interactice_segment;

import static android.graphics.Color.YELLOW;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
    private String ip_port = null;
    private Bitmap bitmap;
    private DrawingView drawingView;
    private ZoomableImageView imageView;
    private FrameLayout frameLayout;

    private IInteractivePresenter presenter;

    private int method = 0; //0 - 图片缩放、移动， 1 - 点击， 2 - 连线， 3 - 画笔
    private int is_positive = 1;
    private int resetImg = 0;

    private float scale = 1; //bitmap填充后的缩放因子

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
        Button btn_finish = findViewById(R.id.top_finish);
        Button btn_exit = findViewById(R.id.top_exit);

        frameLayout = findViewById(R.id.frame);
        imageView = findViewById(R.id.img_show);
        drawingView = findViewById(R.id.drawing_view);

        Intent intent = this.getIntent();
        ip_port = intent.getStringExtra("IpAndPort");
        presenter = new InteractivePresenter(this, ip_port);
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

        // 在ImageView被图像填充完毕后计算图像缩放因子scale
        imageView.post(new Runnable() {
            @Override
            public void run() {
                float bitmapWidth = bitmap.getWidth();
                float bitmapHeight = bitmap.getHeight();
                float imageViewWidth = imageView.getWidth();
                float imageViewHeight = imageView.getHeight();
                float scale_width = imageViewWidth / bitmapWidth;
                float scale_height = imageViewHeight / bitmapHeight;
                scale = Math.min(scale_width, scale_height);
            }
        });
        final int RED_CONST = 1;
        final int BLUE_CONST = 2;

        // 图像调整按钮与功能
        btn_moveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method = 0;
                drawingView.setMethod(method);
                btn_moveImg.setBackgroundColor(0xFF3F51B5);
                btn_interested.setBackgroundColor(0xE9DCFE);
                btn_uninterested.setBackgroundColor(0xE9DCFE);
                btn_pen.setBackgroundColor(0xE9DCFE);
                btn_lines.setBackgroundColor(0xE9DCFE);

                if(resetImg == 0)
                {
                    showMessage("调整图片");
                    resetImg = 1;
                }
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

        // 兴趣点按钮与功能
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
                showMessage("兴趣点");
                btn_moveImg.setBackgroundColor(0xE9DCFE);
                btn_interested.setBackgroundColor(0xFF3F51B5);
                btn_uninterested.setBackgroundColor(0xE9DCFE);
                btn_pen.setBackgroundColor(0xE9DCFE);
                btn_lines.setBackgroundColor(0xE9DCFE);

                drawingView.setColor(RED_CONST);
            }
        });

        // 非兴趣点按钮与功能
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
                drawingView.setMethod(method);
                showMessage("非兴趣点");
                btn_moveImg.setBackgroundColor(0xE9DCFE);
                btn_interested.setBackgroundColor(0xE9DCFE);
                btn_uninterested.setBackgroundColor(0xFF3F51B5);
                btn_pen.setBackgroundColor(0xE9DCFE);
                btn_lines.setBackgroundColor(0xE9DCFE);

                drawingView.setColor(BLUE_CONST);
            }
        });

        // 连接线按钮与功能
        btn_lines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImg = 0;
                drawingView.clear();
                frameLayout.bringChildToFront(drawingView);
                frameLayout.invalidate();

                Bitmap tempBitmap = imageView.getCurrentBitmap();
                Matrix tempMatrix = imageView.getCurrentMatrix();
                if(tempBitmap != null)
                {
                    drawingView.setBitmap(tempBitmap, tempMatrix, scale);
                }

                //画笔的实现
                method = 2;
                drawingView.setMethod(method);
                showMessage("连接线");
                btn_moveImg.setBackgroundColor(0xE9DCFE);
                btn_interested.setBackgroundColor(0xE9DCFE);
                btn_uninterested.setBackgroundColor(0xE9DCFE);
                btn_pen.setBackgroundColor(0xE9DCFE);
                btn_lines.setBackgroundColor(0xFF3F51B5);

                drawingView.setColor(RED_CONST);
            }
        });

        // 画笔按钮与功能
        btn_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImg = 0;
                drawingView.clear();
                frameLayout.bringChildToFront(drawingView);
                frameLayout.invalidate();

                Bitmap tempBitmap = imageView.getCurrentBitmap();
                Matrix tempMatrix = imageView.getCurrentMatrix();
                if(tempBitmap != null)
                {
                    drawingView.setBitmap(tempBitmap, tempMatrix, scale);
                }

                //画笔的实现
                method = 3;
                drawingView.setMethod(method);
                showMessage("画笔");
                btn_moveImg.setBackgroundColor(0xE9DCFE);
                btn_interested.setBackgroundColor(0xE9DCFE);
                btn_uninterested.setBackgroundColor(0xE9DCFE);
                btn_pen.setBackgroundColor(0xFF3F51B5);
                btn_lines.setBackgroundColor(0xE9DCFE);

                drawingView.setColor(RED_CONST);
            }
        });

        // 撤回按钮与功能
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
                //drawingView.setMethod(0);
                showMessage("撤回一步");
                drawingView.undo();
                if(method == 1)
                {
                    presenter.undo();
                    presenter.getImage(InteractiveActivity.this);
                }
            }
        });

        // 完成交互的按钮与功能
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.bringChildToFront(drawingView);
                frameLayout.invalidate();
                
                resetImg = 0;
                if(method == 1)
                {
                    method = 0;
                    drawingView.setMethod(method);
                    showMessage("完成此物识别");

                    drawingView.clear();
                    presenter.finish();
                    presenter.getImage(InteractiveActivity.this);
                }
                 else if(method != 0)
                {
                    //实现绘画痕迹与图像的融合
                    if(drawingView.getBitmap() != null)
                    {
                        bitmap = Bitmap.createScaledBitmap(drawingView.getBitmap(),
                                bitmap.getWidth(), bitmap.getHeight(), true);
                        imageView.setImageBitmap(bitmap);
                        drawingView.clear();
                        drawingView.resetMatrix();

                        btn_pen.setBackgroundColor(0xE9DCFE);
                        btn_lines.setBackgroundColor(0xE9DCFE);
                        method = 0;
                    }
                }
            }
        });

        // 退出交互的按钮与功能
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("保存并退出");
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
                intent.putExtra("IpAndPort", ip_port);

                startActivity(intent);
                drawingView.clear();
                finish();
            }
        });

        // 监听drawingView的触摸事件，计算得到图像原始大小下的交互位置坐标
        drawingView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();
                    // 计算原始图像的坐标
                    PointF originalCoords = imageView.getOriginalImageCoordinates(x, y);
                    float originalX = originalCoords.x / scale;
                    float originalY = originalCoords.y / scale;
                    Log.d("Click Position", "Original X: " + originalX + ", " +
                            "Original Y: " + originalY);
                    if (originalX < 0 || originalX > bitmap.getWidth() || originalY < 0 ||
                            originalY > bitmap.getHeight())
                    {
                        showMessage("交互不在图像上，请重试");
                        return true;
                    }
                    if(method == 1)
                    {
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
    public void onImageGot(Bitmap bitmap)
    {
        this.bitmap = bitmap;
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onGetFailed() {
        showMessage("图片更新失败");
    }
}
