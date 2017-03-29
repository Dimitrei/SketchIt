package com.martinnazi.sketchit.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Document implements Serializable {
    List<Ellipse> ellipses;
    List<Line> lines;
    List<Rectangle> rectangles;

    public Document() {
        ellipses = new ArrayList<>();
        lines = new ArrayList<>();
        rectangles = new ArrayList<>();
    }

    public List<Ellipse> getEllipses() {
        return ellipses;
    }

    public void setEllipses(List<Ellipse> ellipses) {
        this.ellipses = ellipses;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    public void setRectangles(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }
}