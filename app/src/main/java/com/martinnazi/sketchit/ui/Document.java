package com.martinnazi.sketchit.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.MotionEvent;
import android.view.View;

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
 * The Document class is a representation of the shapes on screen.
 * It is able to be saved and loaded from a file implementing {@link Serializable}.
 */
public class Document extends View implements Serializable {

    private List<Ellipse> ellipses;
    private List<Line> lines;
    private List<Rectangle> rectangles;

    public Document(Context context) {
        super(context);
        ellipses = new ArrayList<>();
        lines = new ArrayList<>();
        rectangles = new ArrayList<>();
    }

    /**
     * TODO: Use this method to find coordinates for lines and shapes
     * * For simplicity just get user begin and end points to draw a line, rectangle, or ellipse.
     * * This way, everything is still serializable.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void addLine(Line line) {
        lines.add(line);
        invalidate();
    }

    public void addEllipse(Ellipse ellipse) {
        ellipses.add(ellipse);
        invalidate();
    }

    public void addRectangle(Rectangle rectangle) {
        rectangles.add(rectangle);
        invalidate();
    }

    public void clear() {
        ellipses.clear();
        lines.clear();
        rectangles.clear();
        //Update screen
    }

    public void save(Uri document, ContentResolver contentResolver) {
        ParcelFileDescriptor PFD = null;
        FileOutputStream FOS = null;
        ObjectOutputStream OOS = null;
        try {
            PFD = contentResolver.openFileDescriptor(document, "w"); //Open document with write capabilities
            FOS = new FileOutputStream(PFD.getFileDescriptor());
            OOS = new ObjectOutputStream(FOS);
            OOS.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close streams and file descriptor
            try {
                //Check for unopened streams.. ex) FnF (File Not Found) exception
                closeOutputResources(PFD, FOS, OOS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Document Load(Uri document, ContentResolver contentResolver) {
        Document loadedDocument = null;
        ParcelFileDescriptor PFD = null;
        FileInputStream FIS = null;
        ObjectInputStream OIS = null;
        try {
            PFD = contentResolver.openFileDescriptor(document, "r"); //Open document with read capabilities
            FIS = new FileInputStream(PFD.getFileDescriptor());
            OIS = new ObjectInputStream(FIS);
            try {
                loadedDocument = (Document) OIS.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close streams and file descriptor
            try {
                //Check for unopened streams.. ex) FnF (File Not Found) exception
                closeInputResources(PFD, FIS, OIS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadedDocument;
    }

    private void closeInputResources(ParcelFileDescriptor pfd, FileInputStream fis, ObjectInputStream ois) throws IOException {
        if (pfd != null) {
            pfd.close();
        }
        if (fis != null) {
            fis.close();
        }
        if (ois != null) {
            ois.close();
        }
    }

    private void closeOutputResources(ParcelFileDescriptor pfd, FileOutputStream fos, ObjectOutputStream oos) throws IOException {
        if (pfd != null) {
            pfd.close();
        }
        if (fos != null) {
            fos.close();
        }
        if (oos != null) {
            oos.close();
        }
    }
}
