package sun.bob.pooredit.views;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
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
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/12/1.
 */
public class Todo extends BaseContainer {

    private LinearLayout container;
    private CheckBox checkBox;
    private Text text;
    private boolean checked = false;

    public Todo(Context context) {
        super(context);
    }

    public Todo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initUI() {
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.HORIZONTAL);
        checkBox = new CheckBox(getContext());
        checkBox.setChecked(false);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
            }
        });
        container.addView(checkBox);
        text = new Text(getContext()).setIsChild(true);
        text.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1.0));
        container.addView(text);
        this.addView(container);
    }

    public Todo setChecked(boolean checked){
        this.checkBox.setChecked(checked);
        this.checked = checked;
        return this;
    }

    public Todo setText(CharSequence content){
        text.setText(content);
        return this;
    }

    public void focus(){
        text.requestFocus();
    }

    @Override
    protected void setType() {
        this.type = Constants.TYPE_TODO;
    }

    @Override
    public Object getJsonBean() {
        return new TodoBean().setText(text.baseText.getText())
                .setBackground(text.background)
                .setColor(text.color)
                .setLength(text.baseText.length())
                .setChecked(checked);
    }

    @Override
    public boolean isEmpty() {
        return text.isEmpty();
    }

    class TodoBean extends ElementBean{

        boolean checked;
        private String text;

        private int color;
        private int background;
        private long length;

        private ArrayList<Integer> starts;
        private ArrayList<Integer> ends;
        private ArrayList<Integer> styles;

        public TodoBean() {
            super();
            starts = new ArrayList<>();
            ends = new ArrayList<>();
            styles = new ArrayList<>();
        }

        @Override
        public ElementBean setType() {
            this.type = Constants.TYPE_TODO;
            return this;
        }

        public String getText() {
            return text;
        }

        public TodoBean setText(CharSequence text) {
            //Save styles which html util can not handle.
            Spanned spanned = (Spanned) text;
            for (CharacterStyle style : spanned.getSpans(0, text.length(), CharacterStyle.class)){
                if (style instanceof BackgroundColorSpan) {
                    styles.add(ToolBar.StyleButton.HIGHLIGHT);
                }
                if (style instanceof UnderlineSpan){
                    styles.add(ToolBar.StyleButton.UNDERLINE);
                }
                if (style instanceof StrikethroughSpan){
                    styles.add(ToolBar.StyleButton.STROKE);
                }
                starts.add(spanned.getSpanStart(style));
                ends.add(spanned.getSpanEnd(style));
            }
            this.text = Html.toHtml((Spanned) text);
            return this;
        }


        public int getColor() {
            return color;
        }

        public TodoBean setColor(int color) {
            this.color = color;
            return this;
        }

        public int getBackground() {
            return background;
        }

        public TodoBean setBackground(int background) {
            this.background = background;
            return this;
        }


        public long getLength() {
            return length;
        }

        public TodoBean setLength(long length) {
            this.length = length;
            return this;
        }

        public boolean isChecked() {
            return checked;
        }

        public TodoBean setChecked(boolean checked) {
            this.checked = checked;
            return this;
        }
    }
}
