package sun.bob.pooredit.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sun.bob.pooredit.PoorEdit;
import sun.bob.pooredit.R;
import sun.bob.pooredit.beans.ElementBean;
import sun.bob.pooredit.drawables.BackgroundBorder;
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/12/1.
 */
public class File extends BaseContainer {

    protected String filePath;
    private String name;
    private java.io.File ioFile;
    private LinearLayout container;
    private ImageView icon;
    private TextView fileNameView;
    private TextView fileSizeView;
    private boolean empty = true;

    public File(Context context) {
        super(context);
    }

    public File(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initUI() {
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 5, 0, 5);
        container.setLayoutParams(layoutParams);
        icon = new ImageView(getContext());
        icon.setImageResource(R.drawable.file);

        LinearLayout fileContainer = new LinearLayout(getContext());
        fileContainer.setOrientation(LinearLayout.VERTICAL);

        fileNameView = new TextView(getContext());
        fileNameView.setText(" Click to Select File");
        fileNameView.setGravity(Gravity.CENTER_VERTICAL);
        fileNameView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 2.0));

        fileSizeView = new TextView(getContext());
        fileSizeView.setGravity(Gravity.CENTER_VERTICAL);
        fileSizeView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1.0));

        fileContainer.addView(fileNameView);
        fileContainer.addView(fileSizeView);

        container.addView(icon);
        container.addView(fileContainer);
        this.addView(container);

        this.setBackground(BackgroundBorder.getInstance());

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFunction(v);
            }
        });
    }

    private boolean onClickFunction(View v){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            ((Activity) getContext()).startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), Constants.REQ_PICK_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
        PoorEdit.picking = File.this;
        return true;
    }

    @Override
    protected void setType() {
        this.type = Constants.TYPE_ATT;
    }

    @Override
    public Object getJsonBean() {
        return new FileBean().setFileName(name).setFilePath(filePath);
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public void focus() {

    }

    public String getFilePath() {
        return filePath;
    }

    public File setFilePath(String filePath) {
        this.ioFile = new java.io.File(filePath);
        this.filePath = filePath;
        this.name = ioFile.getName();
        this.fileNameView.setText(name);
        this.fileSizeView.setText(new StringBuilder().append("Size:").append(ioFile.length() / 1024 / 1024).append("MB").toString());
        this.setOnClickListener(null);
        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onClickFunction(v);
            }
        });
        empty = false;
        // TODO: 15/12/2 Change OnClickListener to open file action.
        return this;
    }

    class FileBean extends ElementBean {

        private String filePath;
        private String fileName;

        public FileBean(){
            super();
        }

        @Override
        public ElementBean setType() {
            this.type = Constants.TYPE_ATT;
            return this;
        }

        public String getFilePath() {
            return filePath;
        }

        public FileBean setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public String getFileName() {
            return fileName;
        }

        public FileBean setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
    }

}
