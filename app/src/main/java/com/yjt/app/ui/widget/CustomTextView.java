package com.yjt.app.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.yjt.app.R;

/**
 * 自适应宽度TextView
 *
 * @author yjt
 */
public class CustomTextView extends TextView {

    private final Paint mPaint = new Paint();
    private float mSize1, mSize2;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray tArray = context.obtainStyledAttributes(attrs,
                                                           R.styleable.AutoScaleTextView, defStyle, 0);
        mSize1 = tArray.getDimension(R.styleable.AutoScaleTextView_minTextSize,
                                     10f);
        tArray.recycle();

        mSize2 = this.getTextSize();
    }

    public void setSize1(float minTextSize) {
        mSize1 = minTextSize;
    }

    private void setTextView(String text, int width) {
        if (width <= 0 || TextUtils.isEmpty(text) || text.length() == 0) {
            return;
        }

        int tWidth = width - this.getPaddingLeft() - this.getPaddingRight();
        final float threshold = 0.5f;
        mPaint.set(this.getPaint());

        while ((mSize2 - mSize1) > threshold) {
            float size = (mSize2 + mSize1) / 2;
            mPaint.setTextSize(size);
            if (mPaint.measureText(text) >= tWidth) {
                mSize2 = size;
            } else {
                mSize1 = size;
            }
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSize1);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        setTextView(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            setTextView(this.getText().toString(), this.getWidth());
        }
    }
}
