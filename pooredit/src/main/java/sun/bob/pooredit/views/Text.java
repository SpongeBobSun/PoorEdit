package sun.bob.pooredit.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.View;
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

    private boolean hotBold = false;
    private boolean hotItalic = false;

    private String selection;
    private int sStart = -2, sEnd = -2;
    private int selectionStyle = -1;

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
        if (italic && bold){
            baseText.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
        } else {
            if (italic){
                baseText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
            }
            if (!bold && !italic){
                baseText.setTypeface(Typeface.DEFAULT);
            }
            if (bold){
                baseText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            }
        }
        return this;
    }

    public Text setItalic(boolean italic) {
        this.italic = italic;
        if (bold && italic){
            baseText.setTypeface(Typeface.DEFAULT, Typeface.BOLD_ITALIC);
        } else {
            if (bold){
                baseText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            }
            if (!italic && !bold){
                baseText.setTypeface(Typeface.DEFAULT);
            }
            if (italic){
                baseText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
            }
        }
        return this;
    }

    public Text setUnderline(boolean underline) {
        this.underline = underline;
        if (underline){
            baseText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        } else {
            // TODO: 15/11/27 Is this correct?
            baseText.setPaintFlags(~Paint.UNDERLINE_TEXT_FLAG);
        }
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

    public boolean isHotBold() {
        return hotBold;
    }

    public Text setHotBold(boolean hotBold) {
        this.hotBold = hotBold;
        return this;
    }

    public boolean isHotItalic() {
        return hotItalic;
    }

    public Text setHotItalic(boolean hotItalic) {
        this.hotItalic = hotItalic;
        return this;
    }

    public String getSelection() {
        return selection;
    }

    public int getSelectionStyle() {
        return selectionStyle;
    }

    public Text applySelectionStyle(int style){
        if (sStart < 0 || sEnd < 0){
            selection = null;
            return this;
        }
        switch (style){
            case ToolBar.StyleButton.BOLD:
                baseText.getText().setSpan(new StyleSpan(Typeface.BOLD), sStart, sEnd, Typeface.BOLD);
                selectionStyle = ToolBar.StyleButton.BOLD;
                break;
            case ToolBar.StyleButton.ITALIC:
                baseText.getText().setSpan(new StyleSpan(Typeface.ITALIC), sStart, sEnd, Typeface.ITALIC);
                selectionStyle = ToolBar.StyleButton.ITALIC;
                break;
            case ToolBar.StyleButton.DEFAULT:
                baseText.getText().setSpan(new StyleSpan(Typeface.DEFAULT.getStyle()), sStart, sEnd, Typeface.DEFAULT.getStyle());
                selectionStyle = ToolBar.StyleButton.DEFAULT;
                break;
        }
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
            this.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        EditView.editing = Text.this;
                    }
                }
            });
            this.addTextChangedListener(new TextChangeListener());
        }

        @Override
        public void onSelectionChanged(int start, int end){
            if (start == end){
                selection = null;
                sStart = -2;
                sEnd = -2;
                selectionStyle = -1;
                return;
            }
            boolean hasBold = false, hasItalic = false, hasBoth = false;
            for (StyleSpan span : getText().getSpans(start, end, StyleSpan.class)){
                if (span.getStyle() == Typeface.BOLD){
                    hasBold = true;
                }
                if (span.getStyle() == Typeface.ITALIC){
                    hasItalic = true;
                }
                if (span.getStyle() == Typeface.BOLD_ITALIC){
                    hasBoth = true;
                }
            }
            if (hasBoth){
                selectionStyle = ToolBar.StyleButton.BOLD | ToolBar.StyleButton.ITALIC;
            }
            if (hasBold){
                selectionStyle = ToolBar.StyleButton.BOLD;
            }
            if (hasItalic){
                selectionStyle = ToolBar.StyleButton.ITALIC;
            }
            selection = String.valueOf(getText().subSequence(start, end));
            sStart = start;
            sEnd = end;
        }


        class TextChangeListener implements TextWatcher{

            private int charCount = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(charCount!=BaseText.this.length())
                {
                    charCount = BaseText.this.length();
                    SpannableString ss = new SpannableString(s);
                    if(hotBold && !hotItalic)
                    {
                        ss.setSpan(new StyleSpan(Typeface.BOLD), start, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.setText(ss);
                    }
                    if (hotItalic && !hotBold){
                        ss.setSpan(new StyleSpan(Typeface.ITALIC), start, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.setText(ss);
                    }
                    if (hotItalic && hotBold){
                        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.setText(ss);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                setSelection(s.length());
            }
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

