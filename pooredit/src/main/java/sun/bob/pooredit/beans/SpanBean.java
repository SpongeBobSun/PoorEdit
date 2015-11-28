package sun.bob.pooredit.beans;

/**
 * Created by bob.sun on 15/11/27.
 */
public class SpanBean implements Comparable {
    private int start, end;

    public SpanBean(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    @Override
    public int compareTo(Object another) {
        SpanBean other = (SpanBean) another;
        int ret;
        ret = start - other.start;
        if (ret != 0){
            return ret;
        }
        ret = end - other.end;
        return ret;
    }

    @Override
    public boolean equals(Object another){
        SpanBean sb = (SpanBean) another;
        return start == sb.start && end == sb.end;
    }
}
