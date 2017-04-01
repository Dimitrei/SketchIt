package com.martinnazi.sketchit.ui;

/**
 * Created by arkeonet64 on 3/28/2017.
 */

import android.graphics.Color;
import android.graphics.RectF;

import java.io.Serializable;

/**
 *
 */
class Object2D implements Serializable {

    protected PaintSerializable paint;

    protected int color;

    protected float x0;
    protected float y0;
    protected float x1;
    protected float y1;

    /*public Object2D() {
        this.x0 = 0f;
        this.y0 = 0f;
        this.x1 = 0f;
        this.y1 = 0f;

        paint = new PaintSerializable();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10.5f);
    }*/

    public Object2D(float x0, float y0, float x1, float y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;

        this.color = Color.BLACK;

        this.paint = new PaintSerializable();
        this.paint.setAntiAlias(true);
    }

    public RectF getBounds() {
        return new RectF(this.x0, this.y0, this.x1, this.y1);
    }

    public PaintSerializable getPaint() {
        return this.paint;
    }

    public Integer getColor() {
        return this.color;
    }

    public void setColor(Integer color) {
        this.color = color;
        this.paint.setColor(color);
    }
}
