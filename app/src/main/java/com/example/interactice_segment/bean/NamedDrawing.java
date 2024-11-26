package com.example.interactice_segment.bean;

import android.graphics.Bitmap;

public class NamedDrawing {
    public String name;
    public Bitmap drawing;

    public NamedDrawing(String name, Bitmap drawing) {
        this.name = name;
        this.drawing = drawing;
    }
}
