package com.martinnazi.sketchit.ui;

/**
 * Created by arkeonet64 on 3/27/2017.
 */

class Line extends Object2D {

    float width;

    public Line(float x0, float y0, float x1, float y1) {
        super(x0, y0, x1, y1);
        this.width = 0;
        this.paint.setStrokeWidth(1f);
    }

    public Line(float x0, float y0, float x1, float y1, float width) {
        super(x0, y0, x1, y1);
        this.paint.setStrokeWidth(width);
    }

    public float getLineWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
        this.paint.setStrokeWidth(width);
    }
}
