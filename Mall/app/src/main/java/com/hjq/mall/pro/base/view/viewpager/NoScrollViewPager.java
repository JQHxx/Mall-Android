package com.hjq.mall.pro.base.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *
 * 取消滑动事件
 * */
public class NoScrollViewPager extends ViewPager {

    private boolean isCanScroll = true;
    public NoScrollViewPager(Context context) {
        super(context);
    }
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setNoScroll(boolean noScroll) {
        this.isCanScroll = noScroll;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanScroll){
            return false;
        }else{
            return super.onTouchEvent(arg0);
        }
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll){
            return false;
        }else{
            return super.onInterceptTouchEvent(arg0);
        }
    }

}
