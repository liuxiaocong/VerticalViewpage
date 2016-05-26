package com.example.administrator.verticalviewpage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by LiuXiaocong on 3/31/2016.
 * use for watch page only
 */
public class WatchScrollView extends ScrollView {
    String TAG = "WatchScrollView";
    private int mCurrentScreenHeight = 0;
    private boolean mIsScrolling = false;
    private int mFullScreenHeight = -1;

    public void setCurrentScreenType(TCurrentScreenType currentScreenType) {
        mCurrentScreenType = currentScreenType;
    }

    private TCurrentScreenType mCurrentScreenType = TCurrentScreenType.EMainScreen;


    public interface IOnTouchEventListener {
        void onScrollViewTouchEvent(MotionEvent ev);
    }

    private boolean mScrollable = true;

    private IOnTouchEventListener mIOnTouchEventListener;

    public void setIsScrollable(boolean scrollable) {
        mScrollable = scrollable;
    }

    public void setOnTouchEventListener(IOnTouchEventListener onTouchEventListener) {
        mIOnTouchEventListener = onTouchEventListener;
    }

    public WatchScrollView(Context context) {
        super(context);
    }

    public WatchScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WatchScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSmoothScrollingEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScrollable) return false;
        mIsScrolling = true;
        Log.d(TAG, "MotionEvent:" + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                //mScrollable = false;
                mIsScrolling = false;
                Log.d(TAG, "ACTION_UP");
                break;
        }
        if (mIOnTouchEventListener != null) {
            mIOnTouchEventListener.onScrollViewTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        View child = viewGroup.getChildAt(1);
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        lp.height = screenHeight;
        child.setLayoutParams(lp);
//        if (mCurrentScreenHeight != 0 && mCurrentScreenHeight != screenHeight && mIMyScrollViewListener != null && !mIsScrolling) {
//            View child2 = viewGroup.getChildAt(2);
//            child2.setVisibility(GONE);
//        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mCurrentScreenHeight = b - t;
        mFullScreenHeight = mFullScreenHeight > mCurrentScreenHeight ? mFullScreenHeight : mCurrentScreenHeight;
        Log.d(TAG, "mCurrentScreenHeight:" + mCurrentScreenHeight);
        Log.d(TAG, "mFullScreenHeight:" + mFullScreenHeight);
        Log.d(TAG, "onLayout");

        super.onLayout(changed, l, t, r, b);

        if(mIsScrolling) return;

        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        View firstView = viewGroup.getChildAt(0);
        final int height = firstView.getHeight();

        switch (mCurrentScreenType) {
            case EFirstScreen: {
                Log.d(TAG, "Scroll to EFirstScreen");
                scrollTo(0, 0);
            }
            break;
            case EMainScreen: {
                Log.d(TAG, "Scroll to EMainScreen");
//                post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "scrollToHeight:" + height);
//                        smoothScrollTo(0, height);
//                    }
//                });
//                postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        scrollTo(0, height);
//
//                    }
//                },1000);
                Log.d(TAG, "scroll pre y" + getScrollY());
                scrollTo(0, height);
                Log.d(TAG, "scroll y" + getScrollY());
            }
            break;
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        Log.d(TAG, "overScrollBy y:" + scrollY);
        boolean ret = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
        Log.d(TAG, "overScrollBy ret:" + ret);
        return ret;
    }

    @Override
    public void scrollTo(int x, int y) {
        Log.d(TAG, "scrollTo y:" + y);
        super.scrollTo(x, y);
    }


    //forbid scroll when keybroad up -> screen set to fullscreen
    @Override
    protected void onOverScrolled(int scrollX, int scrollY,
                                  boolean clampedX, boolean clampedY) {
        if (mCurrentScreenHeight != mFullScreenHeight) return;
        Log.d(TAG, "onOverScrolled y:" + scrollY);
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        Log.d(TAG, "onGenericMotionEvent y:" + event.getAction());
        return super.onGenericMotionEvent(event);
    }


}
