package com.vgaw.wraptextview.plan2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.vgaw.wraptextview.R;

/**
 * Created by caojin on 2016/11/7.
 */

/**
 * 使用注意:
 * 图片宽度值 = 组件宽度 - paddingLeft - paddingRight;
 * 图片高度值 = 图片宽度值;
 *
 * 需要优化的地方：
 * 1.目前组件高度需要手动设置，优化后将根据组件的宽度和图片的宽高比例算出图片的高度，
 *      然后自动算出组件的高度，这样组件高度也可以使用wrap_content了;
 * 2.支持所有方向
 */
public class WrapTextView extends TextView {
    private Drawable wrapLeft;
    private Drawable wrapRight;
    private Drawable wrapTop;
    private Drawable wrapBottom;

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;

    private int wrapHeight;
    private int wrapWidth;

    public WrapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WrapTextView);

        wrapLeft = a.getDrawable(R.styleable.WrapTextView_wrapLeft);
        wrapRight = a.getDrawable(R.styleable.WrapTextView_wrapRight);
        wrapTop = a.getDrawable(R.styleable.WrapTextView_wrapTop);
        wrapBottom = a.getDrawable(R.styleable.WrapTextView_wrapBottom);

        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();

        wrapWidth = getWidth() - mPaddingLeft - mPaddingRight;
        wrapHeight = wrapWidth;

        proBounds(wrapLeft);
        proBounds(wrapRight);
        proBounds(wrapTop);
        proBounds(wrapBottom);

        setCompoundDrawables(wrapLeft, wrapTop, wrapRight, wrapBottom);
    }

    private void proBounds(Drawable raw){
        if (raw != null){
            raw.setBounds(0, 0, wrapWidth, wrapHeight);
        }
    }
}
