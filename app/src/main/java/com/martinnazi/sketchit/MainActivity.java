package com.martinnazi.sketchit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.martinnazi.sketchit.ui.Document;

import java.io.File;

/**
 * TODO: Open & Save files (file type is *.sketch)
 * * Use object serialization for this
 * * All drawable objects are added to document
 * TODO: User color for shapes
 * * Option: When item_brush_color is pressed show a dialog box (Most preferred)
 * * Option: When item_brush_color is pressed show a submenu (Least preferred)
 * TODO: User weight for lines
 * * Option: Use OptionsMenu to select weight just like shapes
 * TODO: Implement drawable shapes: Lines, Rectangle, and Ellipses (Circles)
 */

public class MainActivity extends AppCompatActivity {

    final private int OPEN_REQUEST_CODE = 1001;
    final private Intent openFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    final private int SAVE_REQUEST_CODE = 1100;
    final private Intent saveFileIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

    private ActionBar actionBar;
    private File documentFile;
    private Document document;

    /**
     * Called when creating the activity for the application.
     *
     * @param savedInstanceState Instance state when restarting application. Null when not restarting.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar(); //This may not be needed - I just grabbed it in case
        document = new Document(); //Create new document file when activity is created (used to access load/save functions or just to start drawing)
    }

    /**
     * Create Options Menu in the application.
     *
     * @param menu The menu to use to inflate the Options Menu into.
     * @return Returns true to show the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    /**
     * Dispatch incoming result to correct option
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //Get file location
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        Log.i("DEBUG", "Uri: " + uri.toString());
                        //Load documentFile into document
                        document = document.Load(documentFile);
                        Toast.makeText(getApplicationContext(), String.format("Loaded: {%s}",
                                documentFile.getName()), Toast.LENGTH_SHORT).show();
                    } else {
                        //Some error
                    }
                } else {
                    //Some Error
                }
                break;
            case SAVE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        Log.i("DEBUG", "Uri: " + uri.toString());
                        //Write document into documentFile
                        if (documentFile == null) {
                            documentFile = new File(uri.getPath());
                            document.save(documentFile);
                        } else {
                            document.save(documentFile);
                        }
                        Toast.makeText(getApplicationContext(), String.format("Saved: {%s}",
                                documentFile.getName()), Toast.LENGTH_SHORT).show();
                    } else {
                        //Some error
                    }
                } else {
                    //Some Error
                }
                break;
            default:
                break;
        }
    }

    /**
     * Handle OptionsMenu item selection from the ActionBar.
     *
     * @param item The menu item pressed.
     * @return Returns true to end menu processing here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * All MenuItems are in menu_options.xml
         */
        switch (item.getItemId()) {
            case R.id.item_open:
                openFileChooser();
                break;
            case R.id.item_save:
                /**
                 * If new Document
                 * * show file saving dialog & save to location
                 * else
                 * * save file to current file name & directory
                 */
                if (documentFile == null) {
                    saveFileChooser();
                } else {
                    /**
                     * Write data to document
                     */
                }
                break;
            /**
             * Do you want to have an alertDialogBox show on this selection?
             */
            case R.id.item_brush_color:
                break;
            /**
             * Below are the shape selectors in the SubMenu Shape (Do you want to stay with this option?)
             */
            case R.id.item_shape_line:
                item.setChecked(true);
                break;
            case R.id.item_shape_circle:
                item.setChecked(true);
                break;
            case R.id.item_shape_rectangle:
                item.setChecked(true);
                break;
            case R.id.item_clear_image:
                break;
        }
        return true;
    }

    private void saveFileChooser() {
        saveFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        saveFileIntent.setType("*/*");
        saveFileIntent.putExtra(Intent.EXTRA_TITLE, "new_document.sketch");
        startActivityForResult(saveFileIntent, SAVE_REQUEST_CODE);
    }

    private void openFileChooser() {
        openFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        openFileIntent.setType("*/*");
        startActivityForResult(openFileIntent, OPEN_REQUEST_CODE);
    }
}
