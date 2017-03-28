package com.martinnazi.sketchit.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import android.util.TypedValue;
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
 * TODO: Implement drawable shapes: Lines, Rectangle, and Ellipses (Circles)
 */
public class Document extends View implements Serializable {

    private List<Ellipse> ellipses;
    private List<Line> lines;
    private List<Rectangle> rectangles;

    public Document(Context context) {
        super(context);
        setup();
    }

    public Document(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public Document(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private void setup() {
        ellipses = new ArrayList<>();
        lines = new ArrayList<>();
        rectangles = new ArrayList<>();
    }

    /**
     * TODO: Draw shapes in onDraw
     * * Add new shape through onTouch
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, getResources().getDisplayMetrics()));
        float x0 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, getResources().getDisplayMetrics());
        float y0 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, getResources().getDisplayMetrics());
        float x1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, getResources().getDisplayMetrics());
        float y1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, getResources().getDisplayMetrics());
        canvas.drawLine(x0, y0, x1, y1, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        invalidate();
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
