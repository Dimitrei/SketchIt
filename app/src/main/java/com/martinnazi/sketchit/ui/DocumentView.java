package com.martinnazi.sketchit.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by arkeonet64 on 3/27/2017.
 */

/**
 * The Document class is a representation of the shapes on screen.
 * It is able to be saved and loaded from a file implementing {@link Serializable}.
 */
public class DocumentView extends View {
    private Object2D currentObject2D;
    private Document document;
    private String object2DType;
    private float firstTouchX;
    private float firstTouchY;
    private float lastTouchX;
    private float lastTouchY;

    public DocumentView(Context context) {
        super(context);
        setup();
    }

    public DocumentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public DocumentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    public void setObject2DType(String object2DType) {
        this.object2DType = object2DType;
    }

    private void setup() {
        this.object2DType = "Line";
        document = new Document();
    }

    /**
     * Draw all {@link Object2D} subclasses
     * TODO: Bug - Z-Order is messed up.
     * * Option: Add FILO List to Document and in onDraw ONLY draw what the user drew from first to last
     * * * With last being the top-most object
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < document.getLines().size(); i++) {
            Line line = document.getLines().get(i);
            canvas.drawLine(line.getBounds().left,
                    line.getBounds().top,
                    line.getBounds().right,
                    line.getBounds().bottom,
                    line.getPaint());
        }
        for (int i = 0; i < document.getEllipses().size(); i++) {
            Ellipse ellipse = document.getEllipses().get(i);
            canvas.drawOval(ellipse.getBounds(),
                    ellipse.getPaint());
        }
        for (int i = 0; i < document.getRectangles().size(); i++) {
            Rectangle rectangle = document.getRectangles().get(i);
            canvas.drawRect(rectangle.getBounds(),
                    rectangle.getPaint());
        }
    }

    /**
     * TODO: Use this method to find coordinates for lines and shapes
     * * For simplicity just get user begin and end points to draw a line, rectangle, or ellipse.
     * * This way, everything is still serializable.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            /**
             * Used to get starting x, y coordinate
             */
            case MotionEvent.ACTION_DOWN:
                Log.d("DEBUG", "onTouchEvent: Pressed");
                firstTouchX = event.getX();
                firstTouchY = event.getY();

                if (object2DType == "Line") {
                    currentObject2D = new Line(firstTouchX, firstTouchY, firstTouchX, firstTouchY);
                    addLine((Line) currentObject2D);
                } else if (object2DType == "Ellipse") {
                    currentObject2D = new Ellipse(firstTouchX, firstTouchY, firstTouchX, firstTouchY);
                    addEllipse((Ellipse) currentObject2D);
                } else if (object2DType == "Rectangle") {
                    currentObject2D = new Rectangle(firstTouchX, firstTouchY, firstTouchX, firstTouchY);
                    addRectangle((Rectangle) currentObject2D);
                }
                break;
            /**
             * Used to get other points for future implementation of Paths (free-form drawing)
             * Used to get pending end-point for on-screen viewing
             */
            case MotionEvent.ACTION_MOVE:
                lastTouchX = event.getX();
                lastTouchY = event.getY();

                if (currentObject2D instanceof Ellipse) {
                    Ellipse ellipse = (Ellipse) currentObject2D;
                    ellipse.x0 = firstTouchX;
                    ellipse.y0 = firstTouchY;

                    ellipse.x1 = lastTouchX;
                    ellipse.y1 = lastTouchY;

                    ellipse.setColor(Color.GREEN);
                } else if (currentObject2D instanceof Line) {
                    Line line = (Line) currentObject2D;
                    line.x0 = firstTouchX;
                    line.y0 = firstTouchY;

                    line.x1 = lastTouchX;
                    line.y1 = lastTouchY;

                    /**
                     * TODO: Replace the method call below when line widths are implemented.
                     */
                    line.setWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
                    /**
                     * TODO: Replace the method call below when colors are implemented.
                     */
                    line.setColor(Color.BLUE);
                } else if (currentObject2D instanceof Rectangle) {
                    Rectangle rectangle = (Rectangle) currentObject2D;
                    rectangle.x0 = firstTouchX;
                    rectangle.y0 = firstTouchY;

                    rectangle.x1 = lastTouchX;
                    rectangle.y1 = lastTouchY;

                    rectangle.setColor(Color.RED);
                }
                invalidate();
                break;
            /**
             * Used to get ending x, y coordinate
             * * Create shape using start & end points
             */
            case MotionEvent.ACTION_UP:
                lastTouchX = event.getX();
                lastTouchY = event.getY();

                Log.d("DEBUG", "onTouchEvent: Released");
                /**
                 * Determine current selected shape & create the proper shape.
                 */
                if (currentObject2D instanceof Ellipse) {
                    Ellipse ellipse = (Ellipse) currentObject2D;

                    ellipse.x1 = lastTouchX;
                    ellipse.y1 = lastTouchY;

                    ellipse.setColor(Color.GREEN);
                } else if (currentObject2D instanceof Line) {
                    Line line = (Line) currentObject2D;

                    line.x1 = lastTouchX;
                    line.y1 = lastTouchY;

                    /**
                     * TODO: Replace the method call below when line widths are implemented.
                     */
                    line.setWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
                    /**
                     * TODO: Replace the method call below when colors are implemented.
                     */
                    line.setColor(Color.BLUE);
                } else if (currentObject2D instanceof Rectangle) {
                    Rectangle rectangle = (Rectangle) currentObject2D;

                    rectangle.x1 = lastTouchX;
                    rectangle.y1 = lastTouchY;

                    rectangle.setColor(Color.RED);
                }
                break;
        }
        return true;
    }

    /**
     * Adds a {@link Line} to the internal {@link Document}.
     *
     * @param line {@link Line} to add
     */
    public void addLine(Line line) {
        document.getLines().add(line);
        invalidate();
    }

    /**
     * Adds a {@link Ellipse} to the internal {@link Document}.
     *
     * @param ellipse {@link Ellipse} to add
     */
    public void addEllipse(Ellipse ellipse) {
        document.getEllipses().add(ellipse);
        invalidate();
    }

    /**
     * Adds a {@link Rectangle} to the internal {@link Document}.
     *
     * @param rectangle {@link Rectangle} to add
     */
    public void addRectangle(Rectangle rectangle) {
        document.getRectangles().add(rectangle);
        invalidate();
    }

    /**
     * Clears internal {@link Document} and this {@link DocumentView}.
     */
    public void clear() {
        document.getEllipses().clear();
        document.getLines().clear();
        document.getRectangles().clear();
        invalidate();
    }


    /**
     * Saves a {@link Document} from the current {@link DocumentView}.
     *
     * @param document        A {@link Uri} location of the Document file.
     * @param contentResolver A {@link ContentResolver} used by the application/operating system.
     */
    public void save(Uri document, ContentResolver contentResolver) {
        ParcelFileDescriptor PFD = null;
        FileOutputStream FOS = null;
        ObjectOutputStream OOS = null;
        try {
            PFD = contentResolver.openFileDescriptor(document, "w");
            FOS = new FileOutputStream(PFD.getFileDescriptor());
            OOS = new ObjectOutputStream(FOS);
            OOS.writeObject(this.document);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            //Close streams and file descriptor
            try {
                closeOutputResources(PFD, FOS, OOS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads a {@link Document} into the current {@link DocumentView}.
     *
     * @param document        A {@link Uri} location of the Document file.
     * @param contentResolver A {@link ContentResolver} used by the application/operating system.
     */
    public void Load(Uri document, ContentResolver contentResolver) {
        ParcelFileDescriptor PFD = null;
        FileInputStream FIS = null;
        ObjectInputStream OIS = null;
        try {
            PFD = contentResolver.openFileDescriptor(document, "r"); //Open document with read capabilities
            FIS = new FileInputStream(PFD.getFileDescriptor());
            OIS = new ObjectInputStream(FIS);
            try {
                this.document = (Document) OIS.readObject();
                /**
                 * The code block below is for loading shapes back into their saved instances.
                 * Unfortunately {@link PaintSerializable} Does not load back into memory
                 * with the correct Color, and Stroke Width.
                 */
                for (Line line :
                        this.document.lines) {
                    line.setWidth(line.getLineWidth());
                    line.setColor(line.getColor());
                }
                for (Ellipse ellipse :
                        this.document.ellipses) {
                    ellipse.setColor(ellipse.getColor());
                }
                for (Rectangle rectangle :
                        this.document.rectangles) {
                    rectangle.setColor(rectangle.getColor());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close streams and file descriptor
            try {
                closeInputResources(PFD, FIS, OIS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        invalidate();
    }

    /**
     * Closes all used input resources.
     * Specifically, those used in the current {@link DocumentView}.
     *
     * @param pfd A {@link ParcelFileDescriptor} that is used in the load method of {@link DocumentView}.
     * @param fis A {@link FileInputStream} that is used in the load method of {@link DocumentView}.
     * @param ois A {@link ObjectInputStream} that is used in the load method of {@link DocumentView}.
     * @throws IOException Due to this function closing input resources,
     *                     this function needs to be enclosed in try-catch blocks.
     */
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

    /**
     * Closes all used input resources.
     * Specifically, those used in the current {@link DocumentView}.
     *
     * @param pfd A {@link ParcelFileDescriptor} that is used in the save method of {@link DocumentView}.
     * @param fos A {@link FileOutputStream} that is used in the save method of {@link DocumentView}.
     * @param oos A {@link ObjectOutputStream} that is used in the save method of {@link DocumentView}.
     * @throws IOException Due to this function closing output resources,
     *                     this function needs to be enclosed in try-catch blocks.
     */
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
