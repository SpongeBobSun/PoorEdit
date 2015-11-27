package sun.bob.pooredit.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
}