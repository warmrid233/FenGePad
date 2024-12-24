package com.example.interactice_segment.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.interactice_segment.R;
import com.example.interactice_segment.presenter.IInteractivePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DrawingView extends View
{
    private Paint paint;
    private List<float[]> points;
    private int points_num = 0;
    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;
    private int method;

    private ZoomableImageView imageView;


    // 使用栈来记录每次绘制的路径（撤销）
    private Stack<Bitmap> undoStack = new Stack<>();

    public DrawingView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DrawingView(Context context)
    {
        super(context);
    }

    private void init()
    {
        method = 1;
        imageView = findViewById(R.id.img_show);

        paint = new Paint();
        paint.setColor(Color.RED); // 画笔颜色
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3); // 设置画笔宽度
        paint.setStyle(Paint.Style.STROKE); // 设置为描边模式

        paint.setAntiAlias(true);

        path = new Path();
        setDrawingCacheEnabled(true);  // 启用绘制缓存
        points = new ArrayList<>();  // 初始化点的列表
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        // 创建一个Bitmap和Canvas，用于绘制
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas)
    {
        super.onDraw(canvas);
        // 绘制背景图
        canvas.drawBitmap(bitmap, 0, 0, null);

        if (method == 2) {
            drawAllLines();
        }
    }

    public void setMethod(int method)
    {
        this.method = method;
    }

    public void setColor(int color)
    {
        if(color == 1)
        {
            paint.setColor(Color.RED);
        }
        else if (color == 2)
        {
            paint.setColor((Color.BLUE));
        }
    }

    boolean newLine = false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (method == 1)
                {
                    canvas.drawCircle(x, y, 2, paint);
                    saveStateForUndo();
                }
                else if (method == 2)
                {
                    canvas.drawCircle(x, y, 2, paint);
                    // 将点击的坐标添加到列表中
                    points.add(new float[]{x, y});
                } else if (method == 3)
                {
                    path.moveTo(x, y); // 移动到起始点
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (method == 3)
                {
                    path.lineTo(x, y); // 画线
                    canvas.drawPath(path,paint);
                    newLine = true;
                    break;
                }

            case MotionEvent.ACTION_UP:
                // 结束触摸事件时，可以做一些清理操作或保存路径
                if(newLine)
                {
                    saveStateForUndo();
                    newLine = false;
                }
                break;
        }
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (method != 0) {
//                canvas.drawCircle(x, y, 5, paint);
//            }
//            if (method == 1)
//            {
//                saveStateForUndo();
//            }
//            else if (method == 2)
//            {
//                // 将点击的坐标添加到列表中
//                points.add(new float[]{x, y});
//                // 通知视图重绘
//                invalidate();
//                return true;
//            }
//        }

        invalidate();  // 重新绘制视图
        return true;
    }

    // 重新绘制所有的点和线
    private void drawAllLines()
    {
        // 如果有两个以上的点，绘制路径
        if (points.size() > 1)
        {
            path.reset();
            float[] firstPoint = points.get(0);
            path.moveTo(firstPoint[0], firstPoint[1]);
            for (int i = 1; i < points.size(); i++)
            {
                float[] point = points.get(i);
                path.lineTo(point[0], point[1]);
            }
            canvas.drawPath(path, paint);
        }
        if(points.size() > points_num)
        {
            points_num = points.size();
            saveStateForUndo();
        }
    }

    private void saveStateForUndo()
    {
        Bitmap currentBitmap = bitmap.copy(bitmap.getConfig(), true);
        undoStack.push(currentBitmap);
    }

    // 撤销操作
    public void undo()
    {
        // 删除最后一个点
        if (!points.isEmpty() && method == 2)
        {
            points.remove(points.size() - 1);  // 移除最后一个点
            points_num--;
        }

        // 从撤回栈中恢复Bitmap
        if (!undoStack.isEmpty())
        {
            undoStack.pop();  // 弹出栈顶的元素
            if (!undoStack.isEmpty())
            {
                // 恢复栈顶的Bitmap
                bitmap = undoStack.peek().copy(bitmap.getConfig(), true);
            }
            else
            {
                // 如果栈为空，则清空画布
                bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            }

            // 创建新的Canvas，使用恢复后的bitmap
            canvas = new Canvas(bitmap);

            // 刷新视图，更新界面
            invalidate();
        }
    }

    public void clear()
    {
        path.reset();
        points.clear();
        points_num = 0;
        undoStack.clear();
        bitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }
}

