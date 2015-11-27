package sun.bob.pooredit.beans;

/**
 * Created by bob.sun on 15/11/25.
 */
public abstract class ElementBean implements Comparable {
    protected int type;
    private int index;

    public ElementBean(){
        setType();
    }

    public int getType() {
        return type;
    }

    public abstract ElementBean setType();

    public int getIndex() {
        return index;
    }

    public ElementBean setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public int compareTo(Object another){
        return index - ((ElementBean) another).getIndex();
    }
}
