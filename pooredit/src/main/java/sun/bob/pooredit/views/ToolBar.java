package sun.bob.pooredit.views;

import android.content.Context;
import android.util.AttributeSet;
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
            this.addView(new FixSizedImageView(getContext())
                    .setFunction(FixSizedImageView.BOLD)
                    .setImage(R.drawable.bold));
            this.addView(new FixSizedImageView(getContext())
                    .setFunction(FixSizedImageView.ITALIC)
                    .setImage(R.drawable.italic));
            this.addView(new FixSizedImageView(getContext())
                    .setFunction(FixSizedImageView.UNDERLINE)
                    .setImage(R.drawable.underline));
            this.addView(new FixSizedImageView(getContext())
                    .setFunction(FixSizedImageView.TODO)
                    .setImage(R.drawable.todo_list));
            this.addView(new FixSizedImageView(getContext())
                    .setFunction(FixSizedImageView.IMAGE)
                    .setImage(R.drawable.image_file));
            this.addView(new FixSizedImageView(getContext())
                    .setFunction(FixSizedImageView.PDF)
                    .setImage(R.drawable.pdf));
        }
    }

    class FixSizedImageView extends ImageView{

        private int function;

        public FixSizedImageView(Context context) {
            super(context);
            this.setLayoutParams(new LinearLayout.LayoutParams(Constants.TOOLBAR_SIZE, Constants.TOOLBAR_SIZE));
            this.setMaxHeight(Constants.TOOLBAR_SIZE);
            this.setMaxWidth(Constants.TOOLBAR_SIZE);
        }

        public FixSizedImageView setImage(int id){
            this.setImageResource(id);
            return this;
        }

        public FixSizedImageView setFunction(int function) {
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
    }
}
