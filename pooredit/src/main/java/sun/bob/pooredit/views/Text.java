package sun.bob.pooredit.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

    protected BaseText baseText;
    protected int color;
    protected int background;

    private boolean bolding = false;
    private boolean italicing = false;
    private boolean underlining = false;
    private boolean highlighting = false;
    private boolean stroking = false;

    private String selection;
    private int sStart = -2, sEnd = -2;
    private int selectionStyle = -1;

    boolean isChild = false;
    protected HashMap<SpanBean, Integer> styles;

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
        baseText.setHorizontallyScrolling(false);
        baseText.setSingleLine(false);
        baseText.requestFocus();
    }

    public Text setIsChild(boolean isChild) {
        this.isChild = isChild;
        return this;
    }

    public boolean isChild() {
        return isChild;
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

    public Text setUnderlining(boolean underlining) {
        this.underlining = underlining;
        return this;
    }

    public Text setHighlighting(boolean highlighting) {
        this.highlighting = highlighting;
        return this;
    }

    public void setStroking(boolean stroking) {
        this.stroking = stroking;
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
        Editable text = baseText.getText();

        ArrayList<Integer> spans = new ArrayList();

        //Get all existing spans here.
        for (CharacterStyle cs : text.getSpans(sStart, sEnd, CharacterStyle.class)){
            if (cs instanceof StyleSpan){
                spans.add(((StyleSpan) cs).getStyle());
            }
            if (cs instanceof UnderlineSpan){
                spans.add(ToolBar.StyleButton.UNDERLINE);
            }
            if (cs instanceof BackgroundColorSpan){
                spans.add(ToolBar.StyleButton.HIGHLIGHT);
            }
            if (cs instanceof StrikethroughSpan){
                spans.add(ToolBar.StyleButton.STROKE);
            }

        }
        switch (style){
            //Check existing spans to determine the operation is setting or deleting.
            case ToolBar.StyleButton.BOLD:
                if (!spans.contains(Typeface.BOLD)){
                    text.setSpan(new StyleSpan(Typeface.BOLD), sStart, sEnd, Typeface.BOLD);
                    selectionStyle = ToolBar.StyleButton.BOLD;
                } else {
                    for (CharacterStyle span : text.getSpans(sStart, sEnd, CharacterStyle.class)) {
                        if (span instanceof StyleSpan && ((StyleSpan)span).getStyle() == Typeface.BOLD)
                            text.removeSpan(span);
                    }
                }
                break;
            case ToolBar.StyleButton.ITALIC:
                if (!spans.contains(Typeface.ITALIC)){
                    text.setSpan(new StyleSpan(Typeface.ITALIC), sStart, sEnd, Typeface.ITALIC);
                    selectionStyle = ToolBar.StyleButton.ITALIC;
                } else {
                    for (CharacterStyle span : text.getSpans(sStart, sEnd, CharacterStyle.class)) {
                        if (span instanceof StyleSpan && ((StyleSpan)span).getStyle() == Typeface.ITALIC)
                            text.removeSpan(span);
                    }
                }

                break;
            case ToolBar.StyleButton.DEFAULT:
                for (CharacterStyle span : text.getSpans(sStart, sEnd, CharacterStyle.class)) {
                        text.removeSpan(span);
                }
                selectionStyle = ToolBar.StyleButton.DEFAULT;
                break;
            case ToolBar.StyleButton.HIGHLIGHT:
                if (!spans.contains(ToolBar.StyleButton.HIGHLIGHT)){
                    text.setSpan(new BackgroundColorSpan(Color.YELLOW), sStart, sEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    selectionStyle = ToolBar.StyleButton.HIGHLIGHT;
                } else {
                    for (CharacterStyle span : text.getSpans(sStart, sEnd, CharacterStyle.class)) {
                        if (span instanceof BackgroundColorSpan)
                            text.removeSpan(span);
                    }
                }

                break;
            case ToolBar.StyleButton.UNDERLINE:
                if (!spans.contains(ToolBar.StyleButton.UNDERLINE)) {
                    text.setSpan(new UnderlineSpan(), sStart, sEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    selectionStyle = ToolBar.StyleButton.HIGHLIGHT;
                } else {
                    for (CharacterStyle span : text.getSpans(sStart, sEnd, CharacterStyle.class)) {
                        if (span instanceof UnderlineSpan)
                            text.removeSpan(span);
                    }
                }
                break;
            case ToolBar.StyleButton.STROKE:
                if (!spans.contains(ToolBar.StyleButton.STROKE)) {
                    text.setSpan(new StrikethroughSpan(), sStart, sEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    selectionStyle = ToolBar.StyleButton.STROKE;
                } else {
                    for (CharacterStyle span : text.getSpans(sStart, sEnd, CharacterStyle.class)) {
                        if (span instanceof StrikethroughSpan)
                            text.removeSpan(span);
                    }
                }
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
        return new TextBean().setText(baseText.getText())
                .setBackground(background)
                .setColor(color)
                .setLength(baseText.length());
    }


    @Override
    public boolean isEmpty() {
        return baseText.getText().length() == 0;
    }

    @Override
    public void focus() {
        baseText.requestFocus();
    }

    public void setText(CharSequence text){
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
        public boolean onKeyDown(int keyCode, KeyEvent event){
            switch (keyCode) {
                case KeyEvent.KEYCODE_DEL:
                    if (baseText.getSelectionStart() == 0) {
                        EditView.instance.requestDelete(Text.this);
                    }
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    if (isChild){
                        EditView.instance.requestNext(Text.this);
                        return true;
                    }
            }
            return super.onKeyDown(keyCode, event);
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
            boolean hasBold = false, hasItalic = false, hasBoth = false, hasStroke = false;
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
            if (hasStroke){
                selectionStyle = ToolBar.StyleButton.STROKE;
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
            InputMethodManager inputMethodManager;

            public TextChangeListener(){
                inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(charCount != BaseText.this.length()) {
                    charCount = BaseText.this.length();
                    SpannableString ss = new SpannableString(s);

                    if (bolding && !italicing) {
                        ss.setSpan(new StyleSpan(Typeface.BOLD), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.getEditableText().replace(0, s.length(), ss);
                    }
                    if (italicing && !bolding) {
                        ss.setSpan(new StyleSpan(Typeface.ITALIC), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.getEditableText().replace(0, s.length(), ss);
                    }
                    if (italicing && bolding) {
                        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.getEditableText().replace(0, s.length(), ss);
                    }
                    if (underlining){
                        ss.setSpan(new UnderlineSpan(), start, start + count,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.getEditableText().replace(0, s.length(), ss);
                    }
                    if (highlighting){
                        ss.setSpan(new BackgroundColorSpan(Color.YELLOW), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.getEditableText().replace(0, s.length(), ss);
                    }
                    if (stroking){
                        ss.setSpan(new StrikethroughSpan(), start, start + count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        BaseText.this.getEditableText().replace(0, s.length(), ss);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        }

    }

    class TextBean extends ElementBean{

        private String text;

        private int color;
        private int background;
        private long length;

        private ArrayList<Integer> starts;
        private ArrayList<Integer> ends;
        private ArrayList<Integer> styles;


        public TextBean(){
            super();
            starts = new ArrayList<>();
            ends = new ArrayList<>();
            styles = new ArrayList<>();
        }

        public String getText() {
            return text;
        }

        public TextBean setText(CharSequence text) {
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


        public long getLength() {
            return length;
        }

        public TextBean setLength(long length) {
            this.length = length;
            return this;
        }
    }
}

