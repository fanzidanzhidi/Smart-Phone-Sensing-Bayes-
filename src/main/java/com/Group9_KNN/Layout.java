package com.Group9_KNN;


import com.Group9_KNN.Cell;
import com.Group9_KNN.Particle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Layout extends MainActivity{

    public ArrayList<Cell> cells;
    public LinkedList<Particle> particleList;
    public int[] counter;
    boolean converge = false;

    public Layout()
    {
        cells = new ArrayList<Cell>();
        particleList = new LinkedList<Particle>();
        counter = new int[15];
        for (int i = 0; i < 15; i++) {
            counter[i] = 0;
        }
        cells.add(new Cell(1, 2.4f,6.32f, 3.63f, 10.27f));
        cells.add(new Cell(2, 0f,3.16f, 3.63f, 6.32f));
        cells.add(new Cell(3, 0f,0f, 3.63f, 3.16f));
        cells.add(new Cell(4, 3.63f,2.05f, 8.44f, 4.27f));
        cells.add(new Cell(5, 8.44f,2.05f, 13.25f, 4.27f));
        cells.add(new Cell(6, 13.25f,2.05f, 18.06f, 4.27f));
        cells.add(new Cell(7, 18.06f,2.05f, 22.87f, 4.27f));
        cells.add(new Cell(8, 18.54f,4.27f, 22.87f, 6.32f));
        cells.add(new Cell(9, 22.87f,0f, 26.5f, 3.16f));
        cells.add(new Cell(10, 22.87f,3.16f, 26.5f, 6.32f));
        cells.add(new Cell(11, 22.87f,6.32f, 24.1f, 10.27f));
        cells.add(new Cell(12, 18.28f,10.27f, 24.1f, 11.85f));
        cells.add(new Cell(13, 14.5f,4.27f, 16.83f, 8.6f));
        cells.add(new Cell(14, 14.5f,4.27f, 16.83f, 8.6f));
        cells.add(new Cell(15, 14.5f,4.27f, 16.83f, 8.6f));
    }


    public void draw_layout(Canvas canvas, Layout layout, int width, int height, int direction, int top_wall, int left_wall)
    {
        if(!converge)
        {
            for(Cell cell: cells)
            {
                cell.draw_cell(canvas);
            }
            if(particleList.size() == 0)
            {
                P_initialize(canvas, layout, width, height, top_wall, left_wall);
            }
//            else if(particleList.size() > 0 && particleList.size() < 5)
//            {
//                converge = true;
//            }
            else
            {
                LinkedList<Particle> particles;
                particles = new LinkedList<>();
                P_update(direction, particles);
                //Log.d("1111", String.valueOf(particleList.size()));
                for(Particle particle: particleList)
                {
                    if(!particle.is_valid(this))
                    {
                        particle.clean_particle(canvas);
                    }
                    else
                    {
                        particle.draw_particle(canvas);
                    }
                }
            }
        }
    }

    /**
     * generate random particles in each cell
     * @param canvas
     */
    public LinkedList<Particle> P_initialize(Canvas canvas, Layout layout, int width, int height, int top_wall, int left_wall)
    {
        Random rand = new Random();

        for(int i = 0; i < 300; i++)
        {
            Particle particle = new Particle();
            int x = rand.nextInt(width) + left_wall;
            int y = rand.nextInt(height) + top_wall;
            particle.set(x, y, 0, 1);
            if(particle.is_valid(layout))
            {
                particle.draw_particle(canvas);
                particleList.add(particle);
                int id = particle.get_id(layout);
                //Log.d("2222", String.valueOf(id));
            }

        }
        Log.d("33333",String.valueOf(particleList.size()));
        return particleList;
    }

    public void P_update(int direction, LinkedList<Particle> particles)
    {
        //Log.d("p_update","22222");

        for(Particle particle: particleList) {
            // System.out.print(particle.p_x);
            // Log.d("33333","33333");
            particle.p_direction = direction;
            particle.move(1,35);
            if (particle.is_valid(this))
            {
                particle.p_weight ++;
                particles.add(particle);
            }
            else
            {
                particle.p_weight = 0;
            }
        }

        int particle_max = particleList.getFirst().p_weight;
        for(Particle particle_in: particleList)
        {
            if(particle_in.p_weight > particle_max)
                particle_max = particle_in.p_weight;
        }


        int new_x = particleList.getFirst().p_x, new_y = particleList.getFirst().p_y;
        for(Particle particle_in: particleList)
        {
            if(particle_in.p_weight == particle_max)
            {
                new_x = particle_in.p_x;
                new_y = particle_in.p_y;

//                Log.d("11111111", String.valueOf(new_x));
//                Log.d("22222222222", String.valueOf(new_y));
            }
        }

        for(Particle particle: particleList)
        {
            if(particle.p_weight == 0)
            {
                //Log.d("2222", "dead");
                //Log.d("3333", String.valueOf(particle_max));
                //Log.d("3333", String.valueOf(particle_max));
                particle.set(add_noise(new_x, 100), add_noise(new_y, 100), direction, 1);
                particles.add(particle);
            }
        }
        //Log.d("2222", "update finish");
        particleList = particles;
        Log.d("444444",String.valueOf(particleList.size()));
    }

    public void create_canvas()
    {
        // get the screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
    }

    public int count_cell()
    {
        for (int i = 0; i < 15; i++) {
            counter[i] = 0;
        }
        for(Particle particle:particleList)
        {
            for(int i=0; i<15; i++)
            {
                if(particle.get_id(this) == (i+1))
                {
                    counter[i]++;
                }
            }
        }
        int index = 0;
        for(int i=0; i<15; i++)
        {
            if(counter[index]<counter[i])
            {
                index = i;
            }
        }
        return (index+1);
    }

    public void resample()
    {

    }

    public int add_noise(int mean, float variance)
    {
        java.util.Random r = new java.util.Random();
        int noise = (int) (r.nextGaussian() * Math.sqrt(variance) + mean);
        //Log.d("noise", String.valueOf(noise));
        return noise;
    }

}

