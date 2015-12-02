package sun.bob.pooredit.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by bob.sun on 15/12/2.
 */
public class Item extends BaseContainer {

    public Item(Context context) {
        super(context);
    }

    public Item(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initUI() {

    }

    @Override
    protected void setType() {

    }

    @Override
    public Object getJsonBean() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
