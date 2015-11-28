package sun.bob.pooredit.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/11/26.
 */
public class EditView extends LinearLayout {

    public static BaseContainer editing = null;
    private int currentIndex = 0;
    public EditView(Context context) {
        super(context);
        initUI();
    }

    public EditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI(){
        this.setOrientation(VERTICAL);
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
        append(new Text(getContext()));
    }

    private Text addTextOn(int index){
        Text text = new Text(getContext());
        this.addView(text, index);
        return text;
    }

    private void append(BaseContainer e){
        this.addView(e);
        currentIndex++;
    }

    public String exportJSON(String where){
        File file = new File(where);
        if (!file.exists()){
            file.mkdirs();
        }
        file = new File(where + "content.json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Object> content = new ArrayList<>();
        Gson gson = new Gson();
        BaseContainer e;
        for (int i = 0; i < getChildCount(); i++){
            e = (BaseContainer) getChildAt(i);
            switch (e.getType()){
                case Constants.TYPE_TEXT:
                    content.add(e.getJsonBean());
                    break;
                default:
                    break;
            }
        }
        FileWriter dos = null;
        try {
            dos = new FileWriter(file);
            dos.write(gson.toJson(content));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (dos != null)
                try {
                    dos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }

        return file.getName();
    }
}