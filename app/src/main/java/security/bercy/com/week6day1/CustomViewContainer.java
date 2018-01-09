package security.bercy.com.week6day1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bercy on 1/8/18.
 */

public class CustomViewContainer extends ViewGroup {
    public CustomViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        measureChildren(widthMeasureSpec, heightMeasureSpec);

        /**
         * 记录如果是warp_content是设置的宽和高
         */
        // viewgroup的宽高
        int width = 0;
        int height = 0;

        // childview的宽高
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;

        int childCount = getChildCount();

        //用于计算左边两个childview的高度和
        int lHeight = 0;
        //用于计算右边两个childview的高度和,最终取最大值
        int rHeight = 0;
        //用于计算上边两个childview的宽度和
        int tWidth = 0;
        //用于计算下面两个childview的宽度和,最终取最大值
        int bWidth = 0;

        /**
         * 根据childview计算出的宽和高,以及设置的margin计算容器的宽和高,主要用于容器是warp_content时
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
//            cWidth = childView.getWidth();// 为什么不是这个????
            cWidth = childView.getMeasuredWidth();// childview的宽
            cHeight = childView.getMeasuredHeight();// childview的高
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            // 上面两个childview
            if (i == 0 || i == 1) {// 上面的宽度
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 2 || i == 3) {// 下面的宽度
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 0 || i == 2) {//左边的高度
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
            if (i == 1 || i == 3) {//右边的高度
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
        }
        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);
        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        // 是精确的类型就直接取得到的值; 不是则用计算的值
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width, modeHight == MeasureSpec.EXACTLY ? sizeHeight : height);


    }

    @Override
    protected void onLayout(boolean b, int l, int i1, int i2, int i3) {
        int childCount = getChildCount();
        int childWidth = 0;
        int childHeight = 0;
        MarginLayoutParams childParams = null;
        /**
         * 遍历所有childview根据其宽和高,以及margin进行布局
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childWidth = childView.getMeasuredWidth();
            childHeight = childView.getMeasuredHeight();
            childParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;
            switch (i) {
                case 0:
                    cl = childParams.leftMargin;
                    ct = childParams.topMargin;
                    break;
                case 1:
//                    cl = getMeasuredWidth() - childWidth - childParams.rightMargin;
                    // getMeasuredWidth()也是可以的,得到的是在ViewGroup里的宽
                    cl = getWidth() - childWidth - childParams.rightMargin;
                    ct = childParams.topMargin;
                    break;
                case 2:
                    cl = childParams.leftMargin;
//                    ct = getMeasuredHeight() - childHeight - childParams.bottomMargin;
                    ct = getHeight() - childHeight - childParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - childWidth - childParams.rightMargin;
                    ct = getHeight() - childHeight - childParams.bottomMargin;
                    break;
            }
            cr = childWidth + cl;
            cb = childHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }
    }


}
