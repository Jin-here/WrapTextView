package com.vgaw.wraptextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by caojin on 2016/11/7.
 */

/**
 * 使用注意:
 * 1.图片高度需手动设置，宽度随便(wrap_content即可);
 * 2.目前只支持top
 * 图片高度值 = 组件高度 - 字体高度 - paddingBottom - paddingTop;
 * 图片宽度值 = 组件宽度 - paddingLeft - paddingRight;
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
        setGravity(Gravity.BOTTOM);
    }

    private void proBounds(Drawable raw){
        if (raw != null){
            raw.setBounds(0, 0, wrapWidth, wrapHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();

        wrapHeight = (int) (getHeight() - mPaddingBottom - mPaddingTop - getTextSize());
        wrapWidth = getWidth() - mPaddingLeft - mPaddingRight;

        proBounds(wrapLeft);
        proBounds(wrapRight);
        proBounds(wrapTop);
        proBounds(wrapBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int compoundPaddingLeft = getCompoundPaddingLeft();
        final int compoundPaddingTop = getCompoundPaddingTop();
        final int compoundPaddingRight = getCompoundPaddingRight();
        final int compoundPaddingBottom = getCompoundPaddingBottom();
        final int scrollX = getScrollX();
        final int scrollY = getScrollY();
        final int right = getRight();
        final int left = getLeft();
        final int bottom = getBottom();
        final int top = getTop();
        final boolean isLayoutRtl = false;
        final int offset = 0;
        final int leftOffset = isLayoutRtl ? 0 : offset;
        final int rightOffset = isLayoutRtl ? offset : 0 ;

        /*
         * Compound, not extended, because the icon is not clipped
         * if the text height is smaller.
         */

        int vspace = bottom - top - compoundPaddingBottom - compoundPaddingTop;
        int hspace = right - left - compoundPaddingRight - compoundPaddingLeft;

        // IMPORTANT: The coordinates computed are also used in invalidateDrawable()
        // Make sure to update invalidateDrawable() when changing this code.
        if (wrapLeft != null) {
            canvas.save();
            canvas.translate(scrollX + mPaddingLeft + leftOffset,
                    scrollY + compoundPaddingTop +
                            (vspace - wrapHeight) / 2);
            wrapLeft.draw(canvas);
            canvas.restore();
        }

        // IMPORTANT: The coordinates computed are also used in invalidateDrawable()
        // Make sure to update invalidateDrawable() when changing this code.
        if (wrapRight != null) {
            canvas.save();
            canvas.translate(scrollX + right - left - mPaddingRight
                            - wrapWidth - rightOffset,
                    scrollY + compoundPaddingTop + (vspace - wrapHeight) / 2);
            wrapRight.draw(canvas);
            canvas.restore();
        }

        // IMPORTANT: The coordinates computed are also used in invalidateDrawable()
        // Make sure to update invalidateDrawable() when changing this code.
        if (wrapTop != null) {
            canvas.save();
            canvas.translate(scrollX + compoundPaddingLeft +
                    (hspace - wrapWidth) / 2, scrollY + mPaddingTop);
            wrapTop.draw(canvas);
            canvas.restore();
        }

        // IMPORTANT: The coordinates computed are also used in invalidateDrawable()
        // Make sure to update invalidateDrawable() when changing this code.
        if (wrapBottom != null) {
            canvas.save();
            canvas.translate(scrollX + compoundPaddingLeft +
                            (hspace - wrapWidth) / 2,
                    scrollY + bottom - top - mPaddingBottom - wrapHeight);
            wrapBottom.draw(canvas);
            canvas.restore();
        }

        super.onDraw(canvas);
    }
}
