package com.Group9_KNN;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;

public class Particle {

    public int p_x;
    public int p_y;
    public int p_direction;
    public int p_weight;

    public Particle()
    {
        this.p_x = 0;
        this.p_y = 0;
        this.p_direction = 0;
        this.p_weight = 0;
    }

    public void set(int x, int y, int direction, int weight)
    {
        this.p_x = x;
        this.p_y = y;
        this.p_direction = direction;
        this.p_weight = weight;
    }

    public void move(int step, int pixel_per_step)
    {
        switch(this.p_direction)
        {
            case 0:
                this.p_x = this.p_x + step * pixel_per_step;
                break;
            case 1:
                this.p_x = this.p_x - step * pixel_per_step;
                break;
            case 2:
                this.p_y = this.p_y + step * pixel_per_step;
                break;
            case 3:
                this.p_y = this.p_y - step * pixel_per_step;
                break;
            default:
                break;
        }
    }

    public void draw_particle(Canvas canvas)
    {
        // System.out.printf("execute here");
        ShapeDrawable cell = new ShapeDrawable(new OvalShape());
        cell.getPaint().setColor(Color.RED);
        cell.setBounds(p_x-8, p_y-8, p_x+8, p_y+8);
        cell.draw(canvas);
    }

    public void clean_particle(Canvas canvas)
    {
        // System.out.printf("execute here");
        ShapeDrawable cell = new ShapeDrawable(new OvalShape());
        cell.getPaint().setColor(Color.RED);
        cell.setBounds(p_x-0, p_y-0, p_x+0, p_y+0);
        cell.draw(canvas);
    }

    public boolean is_valid(Layout layout)
    {
        int i = 0;
        for(Cell cell: layout.cells) {
            if (this.p_x < cell.map(cell.left_wall) || this.p_x > cell.map(cell.right_wall) || this.p_y < cell.map(cell.top_wall) || this.p_y > cell.map(cell.bottom_wall)) {
                i++;
            }
        }
        return i==15?false:true;
    }

    public int get_id(Layout layout)
    {
        int i = 0;
        for(Cell cell: layout.cells){
            i++;
            if (this.p_x > cell.map(cell.left_wall) && this.p_x < cell.map(cell.right_wall) && this.p_y > cell.map(cell.top_wall) && this.p_y < cell.map(cell.bottom_wall)) {
                return i;
            }
        }
        return 0;
    }
}

