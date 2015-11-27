package sun.bob.pooredit.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;

import sun.bob.pooredit.beans.ElementBean;
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/11/23.
 */
public class Text extends BaseContainer{

    private BaseText baseText;
    private boolean bold = false;
    private boolean italic = false;
    private int color;
    private int background;
    private boolean underline;

    public Text(Context context) {
        super(context);
    }

    public Text(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initUI() {
        baseText = new BaseText(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        baseText.setLayoutParams(layoutParams);
        baseText.setBackgroundColor(Color.WHITE);
        this.addView(baseText);
    }


    @Override
    protected void setType() {
        this.type = Constants.TYPE_TEXT;
    }

    public Text setBold(boolean bold) {
        this.bold = bold;
        if (italic){
            baseText.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
        } else {
            baseText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return this;
    }

    public Text setItalic(boolean italic) {
        this.italic = italic;
        if (bold){
            baseText.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
        } else {
            baseText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        }
        return this;
    }

    public Text setUnderline(boolean underline) {
        this.underline = underline;
        baseText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        return this;
    }

    public Text setColor(int color) {
        this.color = color;
        baseText.setTextColor(color);
        return this;
    }

    public Text setBackground(int background) {
        this.background = background;
        baseText.setBackgroundColor(color);
        return this;
    }

    @Override
    public Object getJsonBean() {
        return new TextBean().setText(baseText.getText().toString())
                .setBackground(background)
                .setBold(bold)
                .setColor(color)
                .setItalic(italic);
    }

    @Override
    public boolean isEmpty() {
        return baseText.getText().length() == 0;
    }

    public void setText(String text){
        baseText.setText(text);
    }

    class BaseText extends EditText{
        public BaseText(Context context) {
            super(context);
            initUI();
        }

        public BaseText(Context context, AttributeSet attrs) {
            super(context, attrs);
            initUI();
        }

        private void initUI() {
            this.setBackground(null);
            setGravity(Gravity.LEFT | Gravity.TOP);
        }

    }

    class TextBean extends ElementBean{

        private String text;

        private boolean bold;
        private boolean italic;
        private int color;
        private int background;

        public TextBean(){
            super();
        }

        public String getText() {
            return text;
        }

        public TextBean setText(String text) {
            this.text = text;
            return this;
        }

        @Override
        public ElementBean setType() {
            this.type = Constants.TYPE_TEXT;
            return this;
        }

        public boolean isBold() {
            return bold;
        }

        public TextBean setBold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public boolean isItalic() {
            return italic;
        }

        public TextBean setItalic(boolean italic) {
            this.italic = italic;
            return this;
        }

        public int getColor() {
            return color;
        }

        public TextBean setColor(int color) {
            this.color = color;
            return this;
        }

        public int getBackground() {
            return background;
        }

        public TextBean setBackground(int background) {
            this.background = background;
            return this;
        }
    }
}

