package com.martinnazi.sketchit.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Document implements Serializable {

    private List<Object2D> object2Ds;

    Document() {
        object2Ds = new ArrayList<>();
    }

    public List<Object2D> getObject2Ds() {
        return object2Ds;
    }

    public void setObject2Ds(List<Object2D> object2Ds) {
        this.object2Ds = object2Ds;
    }

}