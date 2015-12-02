package sun.bob.pooredit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import sun.bob.pooredit.drawables.Dot;
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/12/2.
 */
public class Item extends BaseContainer {

    private LinearLayout container;
    private Dot dot;
    private Text text;
    private boolean checked = false;

    public Item(Context context) {
        super(context);
    }

    public Item(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initUI() {
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        dot = new Dot();
        View dotHolder = new View(getContext());
        dotHolder.setBackground(dot);
        dotHolder.setLayoutParams(new LinearLayout.LayoutParams(50, ViewGroup.LayoutParams.MATCH_PARENT));
        container.addView(dotHolder);
        text = new Text(getContext()).setIsChild(true);
        text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 4.0));
        container.addView(text);
        this.addView(container);
    }

    public Item setText(CharSequence content){
        text.setText(content);
        return this;
    }

    public void focus(){
        text.requestFocus();
    }

    @Override
    protected void setType() {
        this.type = Constants.TYPE_ITEM;
    }

    @Override
    public Object getJsonBean() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return text.isEmpty();
    }
}
