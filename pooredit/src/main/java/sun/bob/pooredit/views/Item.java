package sun.bob.pooredit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import sun.bob.pooredit.beans.ElementBean;
import sun.bob.pooredit.beans.SpanBean;
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
        text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 4.0));
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
        try {
            return new ItemBean().setText(text.baseText.getText().toString())
                    .setBackground(text.background)
                    .setColor(text.color)
                    .setLength(text.baseText.length())
                    .setSpans(text.styles)
                    .setStyles(text.styles);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return text.isEmpty();
    }

    class ItemBean extends ElementBean {

        private String text;

        private int color;
        private int background;
        private long length;

        private ArrayList<SpanBean> spans;
        private ArrayList<Integer> styles;

        public ItemBean() {
            super();
        }

        @Override
        public ElementBean setType() {
            this.type = Constants.TYPE_ITEM;
            return this;
        }

        public String getText() {
            return text;
        }

        public ItemBean setText(String text) {
            this.text = text;
            return this;
        }


        public int getColor() {
            return color;
        }

        public ItemBean setColor(int color) {
            this.color = color;
            return this;
        }

        public int getBackground() {
            return background;
        }

        public ItemBean setBackground(int background) {
            this.background = background;
            return this;
        }

        public ItemBean setStyles(HashMap<SpanBean, Integer> map) throws IllegalAccessException {
            styles = new ArrayList<>();
            if (this.spans == null){
                throw new IllegalAccessException("Should call setSpans first!");
            }
            for (SpanBean spanBean : this.spans){
                this.styles.add(map.get(spanBean));
            }
            return this;
        }

        public ArrayList<SpanBean> getSpans() {
            return spans;
        }

        public ItemBean setSpans(HashMap map) {
            spans = new ArrayList<>();
            Iterator iterator = map.keySet().iterator();
            SpanBean toAdd;
            while (iterator.hasNext()){
                toAdd = (SpanBean) iterator.next();
                if (toAdd.getStart() >= length){
                    continue;
                }
                spans.add(toAdd);
            }
            Collections.sort(spans);
            return this;
        }

        public ArrayList<Integer> getStyles() {
            return styles;
        }

        public long getLength() {
            return length;
        }

        public ItemBean setLength(long length) {
            this.length = length;
            return this;
        }

        public boolean isChecked() {
            return checked;
        }
    }
}
