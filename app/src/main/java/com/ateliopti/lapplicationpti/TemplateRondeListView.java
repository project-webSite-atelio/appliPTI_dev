package com.ateliopti.lapplicationpti;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class TemplateRondeListView extends ListView {

    public TemplateRondeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TemplateRondeListView(Context context) {
        super(context);
    }

    public TemplateRondeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}