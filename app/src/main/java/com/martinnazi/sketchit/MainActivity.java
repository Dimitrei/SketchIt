package com.martinnazi.sketchit;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.martinnazi.sketchit.ui.DocumentView;

public class MainActivity extends AppCompatActivity {

    final private int OPEN_REQUEST_CODE = 1001;
    final private Intent openFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
    final private int SAVE_REQUEST_CODE = 1100;
    final private Intent saveFileIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

    private ActionBar actionBar;
    private DocumentView documentView;
    private Uri documentUri;
    /**
     * Used for saving & loading of {@link DocumentView} files.
     */
    private ContentResolver contentResolver;

    /**
     * Called when creating the activity for the application.
     *
     * @param savedInstanceState Instance state when restarting application. Null when not restarting.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        documentView = (DocumentView) findViewById(R.id.document); //Create new document file when activity is created (used to access load/save functions or just to start drawing)
        contentResolver = getContentResolver();
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
     * @param requestCode What triggered an intent to fire
     * @param resultCode  Status code whether it executed fine or an error occured
     * @param data        Intent that was fired
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        documentUri = data.getData();
                        documentView.Load(documentUri, contentResolver);
                        showLoadedMessage();
                    } else {
                        Log.d("DEBUG",
                                "onActivityResult() called with: requestCode = [" + requestCode + "]," +
                                        " resultCode = [" + resultCode + "], data = [" + data + "]");
                    }
                } else {
                    Log.d("DEBUG",
                            "onActivityResult() called with: requestCode = [" + requestCode + "]," +
                                    " resultCode = [" + resultCode + "], data = [" + data + "]");
                }
                break;
            case SAVE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        documentUri = data.getData();
                        documentView.save(documentUri, contentResolver);
                        showSavedMessage();
                    } else {
                        Log.d("DEBUG",
                                "onActivityResult() called with: requestCode = [" + requestCode + "]," +
                                        " resultCode = [" + resultCode + "], data = [" + data + "]");
                    }
                } else {
                    Log.d("DEBUG", "onActivityResult() called with: requestCode = [" + requestCode + "]," +
                            " resultCode = [" + resultCode + "], data = [" + data + "]");
                }
                break;
            default:
                break;
        }
    }

    /**
     * Shows a {@link Toast} to the user indicating the document was loaded.
     */
    private void showLoadedMessage() {
        Toast.makeText(getApplicationContext(), "Loaded document!",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a {@link Toast} to the user indicating the document was saved.
     */
    private void showSavedMessage() {
        Toast.makeText(getApplicationContext(), "Saved document!",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle OptionsMenu item selection from the ActionBar.
     *
     * @param item The menu item pressed.
     * @return Returns {@code true} to end menu processing here.
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
                if (documentUri == null) {
                    saveFileChooser();
                } else {
                    documentView.save(documentUri, contentResolver);
                    showSavedMessage();
                }
                break;
            case R.id.item_save_as:
                saveFileChooser();
                break;
            /**
             * TODO: User weight for lines
             * * Option: Use OptionsMenu to select weight just like Shape submenu.
             */
            case R.id.item_brush_weight:
                break;

            /**
             * TODO: User color for shapes
             * * Option: When item_brush_color is pressed show a dialog box.
             * * TODO: Add selectedColor field to the Document class to indicate which color shapes should be drawn with
             */
            case R.id.item_brush_color:
                break;

            /**
             * The cases below are the shape selectors in the Shape submenu.
             */
            case R.id.item_shape_line:
                item.setChecked(true);
                documentView.setObject2DType(getString(R.string.shape_type_line));
                break;
            case R.id.item_shape_ellipse:
                item.setChecked(true);
                documentView.setObject2DType(getString(R.string.shape_type_ellipse));
                break;
            case R.id.item_shape_rectangle:
                item.setChecked(true);
                documentView.setObject2DType(getString(R.string.shape_type_rectangle));
                break;
            case R.id.item_shape_free_form:
                item.setChecked(true);
                documentView.setObject2DType(getString(R.string.shape_type_free_form));
                break;
            case R.id.item_reset_workspace:
                documentView.clear();
                documentUri = null;
                break;
        }
        return true;
    }

    /**
     * TODO v0.3: Change to a different Activity that allows user to enter in a filename
     * * This will require users to enter a non-blank name, or a name with spaces. && if no *.sketch is present, extension will be added.
     * * This however, will not solve finding document names when opening in our program.
     * * * To get around this, we must implement our own file selection menu (probably a dialogbox with a hierarchical view
     */
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
