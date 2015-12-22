package sun.bob.pooredit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import sun.bob.pooredit.utils.Constants;
import sun.bob.pooredit.views.BaseContainer;
import sun.bob.pooredit.views.EditView;
import sun.bob.pooredit.views.File;
import sun.bob.pooredit.views.Image;
import sun.bob.pooredit.views.ToolBar;

/**
 * Created by bob.sun on 15/11/26.
 */

public class PoorEdit extends LinearLayout{

    PoorEditWidget poorEditWidget;
    public static BaseContainer picking;
    public static Image.ImageLoaderItf imageLoaderItf = null;
    public PoorEdit(Context context) {
        super(context);
        initUI();
    }

    public PoorEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI(){
        this.setOrientation(VERTICAL);
        poorEditWidget = new PoorEditWidget(getContext());
        this.addView(poorEditWidget);
        this.addView(new ToolBar(getContext()));
        this.setBackgroundColor(Color.WHITE);
    }

    public String exportJSON(String where){
        return poorEditWidget.exportJSON(where);
    }

    public void loadJson(String where){
        poorEditWidget.loadJson(where);
    }

    public void setImageLoader(Image.ImageLoaderItf imageLoaderItf) {
        this.imageLoaderItf = imageLoaderItf;
    }

    class PoorEditWidget extends ScrollView {
        EditView editView;
        public PoorEditWidget(Context context) {
            super(context);
            initUI();
        }

        public PoorEditWidget(Context context, AttributeSet attrs) {
            super(context, attrs);
            initUI();
        }

        private void initUI(){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            this.setLayoutParams(layoutParams);
            editView = new EditView(getContext());
            this.addView(editView);
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        public String exportJSON(String where){
            return editView.exportJSON(where);
        }

        public void loadJson(String where){
            editView.loadJson(where);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (picking == null){
            return;
        }
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (data == null){
            return;
        }
        switch (requestCode){
            case Constants.REQ_PICK_IMAGE:
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picPath = cursor.getString(columnIndex);
                cursor.close();
                if (picPath == null || picPath.length() == 0){
                    break;
                }
                ((Image) picking).setImage(picPath, 0);

                break;
            case Constants.REQ_PICK_FILE:
                Uri selectedFile = data.getData();
                ((File) picking).setFilePath(selectedFile.getPath());
                break;
            default:
                break;
        }
    }
}

