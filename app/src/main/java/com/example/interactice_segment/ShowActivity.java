package com.example.interactice_segment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.interactice_segment.presenter.IShowPresenter;
import com.example.interactice_segment.presenter.ShowPresenter;
import com.example.interactice_segment.view.ShowView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowActivity extends BaseActivity implements ShowView
{
    private final int REQUEST_GALLERY = 2;

    private Bitmap bitmap;
    private ImageView imageView;
    private IShowPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews()
    {
        Button upload_btn = findViewById(R.id.btn_upload);
        Button save_btn = findViewById(R.id.btn_download);
        Button interactive_btn = findViewById(R.id.btn_interactive);
        imageView = findViewById(R.id.img_show);

        presenter = new ShowPresenter(this);

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

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });

        interactive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(bitmap != null)
                {
                    presenter.uploadImg(bitmap);
                    File file = new File(getCacheDir(), "temp_image.jpg");
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent();
                    intent.setClass(ShowActivity.this, InteractiveActivity.class);
                    intent.putExtra("imagePath", file.getAbsolutePath());

                    startActivity(intent);
                    finish();
                }
                else
                {
                    showMessage("当前图像为空，无法交互!");
                }
            }
        });

        // imageView.setImageBitmap(yourBitmap);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = getBitmapToSave();
                if (bitmap != null) {
                    presenter.saveBitmap(bitmap, "SavedImage");
                } else {
                    showMessage("图像为空");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null)
        {
            Uri imageUri = data.getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Bitmap getBitmapToSave()
    {
        // Return the bitmap you want to save. In this demo, we assume it's loaded into imageView.
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        return null;
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