package sun.bob.pooredit.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import sun.bob.pooredit.beans.ElementBean;
import sun.bob.pooredit.beans.SpanBean;
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/11/23.
 */
public class Text extends BaseContainer{

    private BaseText baseText;
    private int color;
    private int background;
    private boolean underline;

    private boolean bolding = false;
    private boolean italicing = false;

    private String selection;
    private int sStart = -2, sEnd = -2;
    private int selectionStyle = -1;

    private HashMap<SpanBean, Integer> styles;

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
        styles = new HashMap<>();
    }


    @Override
    protected void setType() {
        this.type = Constants.TYPE_TEXT;
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

    public boolean isBolding() {
        return bolding;
    }

    public Text setBolding(boolean bolding) {
        this.bolding = bolding;
        return this;
    }

    public boolean isItalicing() {
        return italicing;
    }

    public Text setItalicing(boolean italicing) {
        this.italicing = italicing;
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
        boolean invalide = false;
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
                baseText.getText().setSpan(null, sStart, sEnd, 0);
                selectionStyle = ToolBar.StyleButton.DEFAULT;
                break;
            default:
                invalide = true;
        }
        if (!invalide){
            SpanBean sb = new SpanBean(sStart, sEnd);
            Integer s = styles.get(sb);
            if (s == null || s == 0){
                styles.put(sb, style);
            } else {
                styles.remove(sb);
                styles.put(sb, style);
            }
        }
        return this;
    }

    @Override
    public Object getJsonBean() {
        try {
            return new TextBean().setText(baseText.getText().toString())
                    .setBackground(background)
                    .setColor(color)
                    .setSpans(styles)
                    .setStyles(styles);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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
                selectionStyle = ToolBar.StyleButton.BOLD + ToolBar.StyleButton.ITALIC;
            }
            if (hasBold){
                selectionStyle = ToolBar.StyleButton.BOLD;
            }
            if (hasItalic){
                selectionStyle = ToolBar.StyleButton.ITALIC;
            }
            try {
                selection = String.valueOf(getText().subSequence(start, end));
            } catch (IndexOutOfBoundsException e){
                selection = null;
                sStart = -2;
                sEnd = -2;
                return;
            }
            sStart = start;
            sEnd = end;
        }

        class TextChangeListener implements TextWatcher{

            private int charCount = 0;
            private int len;
            private boolean styled = false;
            private int changedStyle = ToolBar.StyleButton.DEFAULT;
            private int lastStart = 0, lastEnd = 0;

//            private StyleSpan bold, italic, boldItalic;
//
//            public TextChangeListener(){
//                bold = new StyleSpan(Typeface.BOLD);
//                italic = new StyleSpan(Typeface.ITALIC);
//                boldItalic = new StyleSpan(Typeface.BOLD_ITALIC);
//            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("s:",  s.toString());
                Log.e("start:", "" +start);
                Log.e("before:", "" + before);
                Log.e("count:", "" + count);

                if(charCount != BaseText.this.length())
                {
                    styled = false;
                    charCount = BaseText.this.length();
                    SpannableString ss = new SpannableString(s);
                    if(bolding && !italicing)
                    {
                        ss.setSpan(new StyleSpan(Typeface.BOLD), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.setText(ss);
                        styled = true;
                        changedStyle = ToolBar.StyleButton.BOLD;
                    }
                    if (italicing && !bolding){
                        ss.setSpan(new StyleSpan(Typeface.ITALIC), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.setText(ss);
                        styled = true;
                        changedStyle = ToolBar.StyleButton.ITALIC;
                    }
                    if (italicing && bolding){
                        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.setText(ss);
                        styled = true;
                        changedStyle = ToolBar.StyleButton.BOLD + ToolBar.StyleButton.ITALIC;
                    }
                    len = start + count;

                    if (styled){
                        if (lastStart == 0 && lastEnd == 0){
                            lastStart = start;
                            lastEnd = start + count;
                            return;
                        }
                        if (start == lastEnd && before == 0 && count == 1){
                            //append
                            lastEnd += 1;
                            return;
                        }
                        if (start == lastEnd - 1 && before == 1 && count == 0){
                            //backspace
                            lastEnd -= 1;
                            return;
                        }
                        if (start != lastEnd && before == 0 && count == 1){
                            //insert
                            lastStart = start;
                            lastEnd = start + count;
                            return;
                        }
                    } else {
                        if (lastStart != 0 && lastEnd != 0 && lastStart - lastEnd != 0){
                            SpanBean sbefore = new SpanBean(lastStart, lastEnd - 1);
                            Integer i = styles.get(sbefore);
                            if (i != null && i != 0){
                                styles.remove(sbefore);
                            }
                            styles.put(new SpanBean(lastStart, lastEnd), changedStyle);
                        }
                        lastEnd = lastStart = 0;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > len){
                    setSelection(len);
                } else {
                    setSelection(s.length());
                }
            }
        }

    }

    class TextBean extends ElementBean{

        private String text;

        private int color;
        private int background;

        private ArrayList<SpanBean> spans;
        private ArrayList<Integer> styles;

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

        public TextBean setStyles(HashMap<SpanBean, Integer> map) throws IllegalAccessException {
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

        public TextBean setSpans(HashMap map) {
            spans = new ArrayList<>();
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()){
                spans.add((SpanBean) iterator.next());
            }
            Collections.sort(spans);
            return this;
        }

        public ArrayList<Integer> getStyles() {
            return styles;
        }
    }
}

