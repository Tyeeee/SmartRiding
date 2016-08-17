
package com.yjt.app.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yjt.app.R;

public class CircleImageView extends ImageView {

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private final Paint mFillPaint = new Paint();
    private final Paint mTextPaint = new Paint();

    private int mBorderColor;
    private int mBorderWidth;
    private boolean isBorderOverlay;
    private int mFillColor;
    private String mText;
    private int mTextColor;
    private int mTextSize;
    private int mTextPadding;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;

    private ColorFilter mColorFilter;

    private boolean isReady;
    private boolean isSetupPending;

    public CircleImageView(Context context) {
        super(context);
        intialize();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
        mBorderWidth = array.getDimensionPixelSize(R.styleable.CircleImageView_border_width, 0);
        mBorderColor = array.getColor(R.styleable.CircleImageView_border_color, Color.BLACK);
        isBorderOverlay = array.getBoolean(R.styleable.CircleImageView_border_overlay, false);
        mFillColor = array.getColor(R.styleable.CircleImageView_fill_color, Color.TRANSPARENT);
        mText = array.getString(R.styleable.CircleImageView_text);
        mTextColor = array.getColor(R.styleable.CircleImageView_border_color, Color.BLACK);
        mTextSize = array.getDimensionPixelSize(R.styleable.CircleImageView_text_size, 22);
        mTextPadding = array.getDimensionPixelSize(R.styleable.CircleImageView_text_padding, 4);
        array.recycle();
        intialize();
    }

    public void intialize() {
        super.setScaleType(ScaleType.CENTER_CROP);
        isReady = true;

        if (isSetupPending) {
            setupPending();
            isSetupPending = false;
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupPending() {
        if (!isReady) {
            isSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }


        if (mBitmap == null && TextUtils.isEmpty(mText)) {
            invalidate();
            return;
        }


        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);


        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);


        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);
        if (!isBorderOverlay) {
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);

        if (mBitmap != null) {
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            mBitmapPaint.setAntiAlias(true);
            mBitmapPaint.setShader(mBitmapShader);
            updateShaderMatrix();
        }
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    public ScaleType getScaleType() {
        return ScaleType.CENTER_CROP;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != ScaleType.CENTER_CROP) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null && TextUtils.isEmpty(mText)) {
            return;
        }

        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mFillPaint);
        }
        if (mBitmap != null) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mBitmapPaint);
        }
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius, mBorderPaint);
        }

        if (!TextUtils.isEmpty(mText)) {
            Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
            canvas.drawText(mText,
                            getWidth() / 2 - mTextPaint.measureText(mText) / 2,
                            getHeight() / 2 - fm.descent + (fm.bottom - fm.top) / 2, mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupPending();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setupPending();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setupPending();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setupPending();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        setupPending();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }

        mColorFilter = cf;
        mBitmapPaint.setColorFilter(mColorFilter);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (!TextUtils.isEmpty(mText)) {
            int textMeasuredSize = (int) (mTextPaint.measureText(mText));
            textMeasuredSize += 2 * mTextPadding;
            if (widthMeasureSpecMode == MeasureSpec.AT_MOST && heightMeasureSpecMode == MeasureSpec.AT_MOST) {
                if (textMeasuredSize > getMeasuredWidth() || textMeasuredSize > getMeasuredHeight()) {
                    setMeasuredDimension(textMeasuredSize, textMeasuredSize);
                }
            }
        }
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor1(@ColorInt int borderColor) {
        if (borderColor != mBorderColor) {
            this.mBorderColor = borderColor;
            mBorderPaint.setColor(mBorderColor);
            invalidate();
        }
    }

    public void setBorderColor2(@ColorRes int borderColorRes) {
        setBorderColor1(getContext().getResources().getColor(borderColorRes));
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth != mBorderWidth) {
            this.mBorderWidth = borderWidth;
            setupPending();
        }
    }

    public boolean isBorderOverlay() {
        return isBorderOverlay;
    }

    public void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay != isBorderOverlay) {
            this.isBorderOverlay = borderOverlay;
            setupPending();
        }
    }

    public int getFillColor() {
        return mFillColor;
    }

    public void setFillColor1(@ColorInt int fillColor) {
        if (fillColor != mFillColor) {
            this.mFillColor = fillColor;
            mFillPaint.setColor(fillColor);
            invalidate();
        }
    }

    public void setFillColor2(@ColorRes int fillColorRes) {
        setFillColor1(getContext().getResources().getColor(fillColorRes));
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
        invalidate();
    }

    public void setText(@StringRes int TextResId) {
        setText(getResources().getString(TextResId));
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(@ColorInt int textColor) {
        this.mTextColor = textColor;
        mTextPaint.setColor(textColor);
        invalidate();
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        mTextPaint.setTextSize(textSize);
        invalidate();
    }

    public int getTextPadding() {
        return mTextPadding;
    }

    public void setTextPadding(int textPadding) {
        this.mTextPadding = textPadding;
        invalidate();
    }
}
