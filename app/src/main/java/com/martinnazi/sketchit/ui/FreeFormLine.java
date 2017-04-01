package com.martinnazi.sketchit.ui;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arkeonet64 on 3/30/2017.
 */

class FreeFormLine extends Object2D {
    float width;
    List<Line> lines;

    public FreeFormLine(float x0, float y0, float x1, float y1, Line line) {
        super(x0, y0, x1, y1);
        lines = new ArrayList<>();
        addLine(line);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        setWidth(10.5f);
    }

    public FreeFormLine(float x0, float y0, float x1, float y1, Line line, int width) {
        super(x0, y0, x1, y1);
        lines = new ArrayList<>();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        addLine(line);
        setWidth(width);
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public float[] getPoints() {
        float[] points = new float[lines.size() * 4];
        for (int i = 0; i / 4 < lines.size(); i++) {

            if (i % 4 == 0) {
                points[i] = lines.get(i / 4).getBounds().left;
            } else if (i % 4 == 1) {
                points[i] = lines.get(i / 4).getBounds().top;
            } else if (i % 4 == 2) {
                points[i] = lines.get(i / 4).getBounds().right;
            } else if (i % 4 == 3) {
                points[i] = lines.get(i / 4).getBounds().bottom;
            }
        }
        return points;
    }

    public float getLineWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
        this.paint.setStrokeWidth(width);
    }
}
