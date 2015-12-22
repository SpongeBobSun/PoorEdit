package sun.bob.pooredit.views;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sun.bob.pooredit.beans.ElementBean;
import sun.bob.pooredit.drawables.Dot;
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/12/19.
 */
public class NumberItem extends BaseContainer {

    private LinearLayout container;
    private int num;
    private TextView numHolder;
    private Text text;

    public NumberItem(Context context) {
        super(context);
    }

    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initUI() {
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        numHolder = new TextView(getContext());
        numHolder.setLayoutParams(new LinearLayout.LayoutParams(50, ViewGroup.LayoutParams.MATCH_PARENT));
        numHolder.setTextSize(20);
        numHolder.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        container.addView(numHolder);
        text = new Text(getContext()).setIsChild(true);
        text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 4.0));
        container.addView(text);
        this.addView(container);
    }

    public int getNum() {
        return num;
    }

    public NumberItem setNum(int num) {
        this.num = num;
        numHolder.setText("" + this.num);
        return this;
    }

    public NumberItem setText(CharSequence content) {
        text.setText(content);
        return this;
    }

    @Override
    protected void setType() {
        this.type = Constants.TYPE_NUM;
    }

    @Override
    public Object getJsonBean() {
        return new NumItemBean().setText(text.baseText.getText())
                .setBackground(text.background)
                .setColor(text.color)
                .setNum(num)
                .setLength(text.baseText.length());
    }

    @Override
    public boolean isEmpty() {
        return text.isEmpty();
    }

    @Override
    public void focus() {
        this.text.focus();
    }

    class NumItemBean extends ElementBean{

        private String text;

        private int color;
        private int background;
        private long length;
        private int num;

        private ArrayList<Integer> starts;
        private ArrayList<Integer> ends;
        private ArrayList<Integer> styles;

        public NumItemBean() {
            super();
            starts = new ArrayList<>();
            ends = new ArrayList<>();
            styles = new ArrayList<>();
        }

        @Override
        public NumItemBean setType() {
            this.type = Constants.TYPE_NUM;
            return this;
        }
        public NumItemBean setText(CharSequence text) {
            //Save styles which html util can not handle.
            Spanned spanned = (Spanned) text;
            for (BackgroundColorSpan bg : spanned.getSpans(0, text.length(), BackgroundColorSpan.class)){
                styles.add(ToolBar.StyleButton.HIGHLIGHT);
                starts.add(spanned.getSpanStart(bg));
                ends.add(spanned.getSpanEnd(bg));
            }
            this.text = Html.toHtml((Spanned) text);
            return this;
        }

        public int getNum() {
            return num;
        }

        public NumItemBean setNum(int num) {
            this.num = num;
            return this;
        }

        public int getColor() {
            return color;
        }

        public NumItemBean setColor(int color) {
            this.color = color;
            return this;
        }

        public int getBackground() {
            return background;
        }

        public NumItemBean setBackground(int background) {
            this.background = background;
            return this;
        }


        public long getLength() {
            return length;
        }

        public ElementBean setLength(long length) {
            this.length = length;
            return this;
        }
    }
}
