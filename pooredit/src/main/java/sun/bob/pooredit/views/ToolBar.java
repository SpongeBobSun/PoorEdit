package sun.bob.pooredit.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import sun.bob.pooredit.R;
import sun.bob.pooredit.utils.Constants;

/**
 * Created by bob.sun on 15/11/27.
 */
public class ToolBar extends HorizontalScrollView {
    public ToolBar(Context context) {
        super(context);
        initUI();
    }

    public ToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI(){
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
        this.addView(new ToolBarWidget(getContext()));
    }

    class ToolBarWidget extends LinearLayout{

        public ToolBarWidget(Context context) {
            super(context);
            this.addView(new StyleButton(getContext())
                    .setFunction(StyleButton.BOLD)
                    .setImage(R.drawable.bold));
            this.addView(new StyleButton(getContext())
                    .setFunction(StyleButton.ITALIC)
                    .setImage(R.drawable.italic));
            this.addView(new StyleButton(getContext())
                    .setFunction(StyleButton.UNDERLINE)
                    .setImage(R.drawable.underline));
            this.addView(new StyleButton(getContext())
                    .setFunction(StyleButton.TODO)
                    .setImage(R.drawable.todo_list));
            this.addView(new StyleButton(getContext())
                    .setFunction(StyleButton.IMAGE)
                    .setImage(R.drawable.image_file));
            this.addView(new StyleButton(getContext())
                    .setFunction(StyleButton.PDF)
                    .setImage(R.drawable.pdf));
        }
    }

    class StyleButton extends ImageView{

        private int function;
        private boolean on = false;

        public StyleButton(Context context) {
            super(context);
            this.setLayoutParams(new LinearLayout.LayoutParams(Constants.TOOLBAR_SIZE, Constants.TOOLBAR_SIZE));
            this.setMaxHeight(Constants.TOOLBAR_SIZE);
            this.setMaxWidth(Constants.TOOLBAR_SIZE);
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Text text = (Text) EditView.editing;
                    if (text == null || !(EditView.editing instanceof Text)){
                        return;
                    }
                    switch (function){
                        case BOLD:
                            if (text.getSelection() != null){
                                if (text.getSelectionStyle() == BOLD){
                                    text.applySelectionStyle(DEFAULT);
                                } else {
                                    text.applySelectionStyle(BOLD);
                                }
                            } else {
                                if (!on) {
                                    //turn on
                                    text.setHotBold(true);
                                    setImageResource(R.drawable.bold_filled);
                                    on = true;
                                } else {
                                    //turn off
                                    text.setHotBold(false);
                                    setImageResource(R.drawable.bold);
                                    on = false;
                                }
                            }
                            break;
                        case ITALIC:
                            if (text.getSelection() != null){
                                if (text.getSelectionStyle() == ITALIC){
                                    text.applySelectionStyle(DEFAULT);
                                } else {
                                    text.applySelectionStyle(ITALIC);
                                }
                            } else {
                                if (!on) {
                                    //turn on
                                    text.setHotItalic(true);
                                    setImageResource(R.drawable.italic_filled);
                                    on = true;
                                } else {
                                    //turn off
                                    text.setHotItalic(false);
                                    setImageResource(R.drawable.italic);
                                    on = false;
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        public StyleButton setImage(int id){
            this.setImageResource(id);
            return this;
        }

        public StyleButton setFunction(int function) {
            this.function = function;
            return this;
        }

        public static final int BOLD = 0x40;
        public static final int ITALIC = 0x41;
        public static final int UNDERLINE = 0x42;
        public static final int TODO = 0x43;
        public static final int IMAGE = 0x44;
        public static final int PDF = 0x45;
        public static final int FILE = 0x46;
        public static final int DEFAULT = 0x47;
    }
}
