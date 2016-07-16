package com.softdesign.devintensive.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.softdesign.devintensive.R;

/**
 * Created by savos on 15.07.2016.
 */

public class AspectRatioImageView extends ImageView {
    private static final float DEFAULT_ASPECT_RATIO = 1.78f;
    private final float mAspectRatio;


    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
        mAspectRatio = typedArray.getFloat(R.styleable.AspectRatioImageView_aspect_ratio, DEFAULT_ASPECT_RATIO);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int newWidth;
        int newHeight;

        newWidth = getMeasuredWidth();
        newHeight = (int) (newWidth/ mAspectRatio);

        setMeasuredDimension(newWidth, newHeight);
    }
}
