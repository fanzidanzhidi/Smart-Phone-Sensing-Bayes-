package com.Group9_KNN;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

public class Cell {

    //define the attributes of each cell
    public int cell_id;
    public float left_wall;
    public float top_wall;
    public float right_wall;
    public float bottom_wall;

    public Cell (int id, float left, float top, float right, float bottom)
    {
        this.cell_id = id;
        this.left_wall = left;
        this.top_wall = top;
        this.right_wall = right;
        this.bottom_wall = bottom;
    }

    public void draw_cell(Canvas canvas)
    {
        int left, top, right, bottom;
        left = map(left_wall);
        top = map(top_wall);
        right = map(right_wall);
        bottom = map(bottom_wall);
        ShapeDrawable cell = new ShapeDrawable(new RectShape());
        cell.getPaint().setStyle(Paint.Style.STROKE);
        cell.getPaint().setColor(Color.BLACK);
        cell.setBounds(left, top, right, bottom);
        cell.draw(canvas);
    }

    public int map(float length)
    {
        return (int)(70*length);
    }
}
