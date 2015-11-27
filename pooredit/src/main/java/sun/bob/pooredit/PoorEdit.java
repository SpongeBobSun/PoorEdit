package sun.bob.pooredit;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import sun.bob.pooredit.views.BaseContainer;
import sun.bob.pooredit.views.EditView;
import sun.bob.pooredit.views.ToolBar;

/**
 * Created by bob.sun on 15/11/26.
 */

public class PoorEdit extends LinearLayout{

    public PoorEdit(Context context) {
        super(context);
        initUI();
    }

    public PoorEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    private void initUI(){
        this.setOrientation(VERTICAL);
        this.addView(new PoorEditWidget(getContext()));
        this.addView(new ToolBar(getContext()));
    }

    class PoorEditWidget extends ScrollView {
        public PoorEditWidget(Context context) {
            super(context);
            initUI();
        }

        public PoorEditWidget(Context context, AttributeSet attrs) {
            super(context, attrs);
            initUI();
        }

        private void initUI(){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            this.setLayoutParams(layoutParams);
            this.addView(new EditView(getContext()));
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }
}

