package com.martinnazi.sketchit.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arkeonet64 on 3/27/2017.
 */

/**
 * Holds all Ellipses, Lines, and Rectangles in Lists
 * Encapsulated lists to load and save all drawables
 */
public class Document implements Serializable {
    private FileOutputStream FOS;
    private ObjectOutputStream OOS;
    private FileInputStream FIS;
    private ObjectInputStream OIS;

    private List<Ellipse> ellipses;
    private List<Line> lines;
    private List<Rectangle> rectangles;


    public Document() {
        ellipses = new ArrayList<>();
        lines = new ArrayList<>();
        rectangles = new ArrayList<>();
    }

    public void clear(){
        ellipses.clear();
        lines.clear();
        rectangles.clear();
        //Update screen
    }

    public void save(File document) {
        try {
            FOS = new FileOutputStream(document);
            OOS = new ObjectOutputStream(FOS);
            OOS.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close streams
            try {
                //Check for unopened streams.. ex) FnF (File Not Found) exception
                if (FOS != null) {
                    FOS.close();
                    FOS = null;
                }
                if (OOS != null) {
                    OOS.close();
                    OOS = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Document Load(File document) {
        Document loadedDocument = null;

        try {
            FIS = new FileInputStream(document);
            OIS = new ObjectInputStream(FIS);
            try {
                loadedDocument = (Document) OIS.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close streams
            try {
                //Check for unopened streams.. ex) FnF (File Not Found) exception
                if (FIS != null) {
                    FIS.close();
                    FIS = null;
                }
                if (OIS != null) {
                    OIS.close();
                    OIS = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadedDocument;
    }
}
