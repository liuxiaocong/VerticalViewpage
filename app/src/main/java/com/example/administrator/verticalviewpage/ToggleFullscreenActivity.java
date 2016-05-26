package com.example.administrator.verticalviewpage;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

public class ToggleFullscreenActivity extends AppCompatActivity {
    private int mScreenWidth;
    private int mScreenHeight;
    private View mWrap1;
    private View mWrap2;
    private View mWrap3;
    SwitchScrollView switchScrollView;
    String TAG = "ToggleFullscreenActivity";
    private int mAffectedDistance = 300;
    private TCurrentTouchAreaType mTCurrentTouchAreaType = TCurrentTouchAreaType.ENone;
    private TCurrentScreenType mTCurrentScreenType = TCurrentScreenType.EMainScreen;
    float mStartY = 0;
    View mRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle_fullscreen_status);
        mRoot = getWindow().getDecorView();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mWrap1 = findViewById(R.id.wrap1);
        mWrap2 = findViewById(R.id.wrap2);
        mWrap3 = findViewById(R.id.wrap3);
        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) mWrap1.getLayoutParams();
        lp1.height = mScreenHeight;
        mWrap1.setLayoutParams(lp1);

        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) mWrap2.getLayoutParams();
        lp2.height = mScreenHeight;
        mWrap2.setLayoutParams(lp2);

        LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) mWrap3.getLayoutParams();
        lp3.height = mScreenHeight;
        mWrap3.setLayoutParams(lp1);

        switchScrollView = (SwitchScrollView) findViewById(R.id.switch_scroll_view);
        switchScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(TAG, "ScrollView onGlobalLayout");
                switchScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                switchScrollView.setOnTouchEventListener(mOnScrollTouchListener);
                switchScrollView.setScrollY(mScreenHeight);
//                /switchScrollView.setIsScrollable(false);
            }
        });

        mRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isKeyboardShown = isKeyboardShown();
                //Log.d(TAG, "onGlobalLayout");
                //Log.d(TAG, "isKeyboardShown:" + isKeyboardShown());
                if(isKeyboardShown)
                {
                    setFullScreen(false);
                }else {
                    setFullScreen(true);
                }
            }
        });
    }

    private boolean isKeyboardShown() {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        mRoot.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = mRoot.getResources().getDisplayMetrics();
        int heightDiff = mRoot.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private SwitchScrollView.IOnTouchEventListener mOnScrollTouchListener = new SwitchScrollView.IOnTouchEventListener() {
        @Override
        public void onScrollViewTouchEvent(MotionEvent ev) {
            switch (ev.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mStartY = ev.getY();
                    Log.d(TAG, "ScrollView ACTION_DOWN");
                    Log.d(TAG, "ScrollView startY:" + mStartY);
                    break;
                case MotionEvent.ACTION_UP:
                    //switchScrollView.setIsScrollable(false);
                    Log.d(TAG, "ScrollView ACTION_UP");
                    float endY = ev.getY();
                    Log.d(TAG, "ScrollView startY:" + mStartY);
                    Log.d(TAG, "ScrollView endY:" + endY);
                    Log.d(TAG, "ScrollView distance:" + (endY - mStartY));
                    if (mTCurrentScreenType.equals(TCurrentScreenType.EMainScreen)) {
                        if (endY - mStartY > mAffectedDistance) {
                            goFirst();
                        } else if (endY - mStartY < -mAffectedDistance) {
                            goNext();
                        } else {
                            goMiddle();
                        }
                    } else if (mTCurrentScreenType.equals(TCurrentScreenType.EFirstScreen)) {
                        if (endY - mStartY < -mAffectedDistance) {
                            goMiddle();
                        } else {
                            goFirst();
                        }
                    } else if (mTCurrentScreenType.equals(TCurrentScreenType.ENextScreen)) {
                        {
                            if (endY - mStartY > mAffectedDistance) {
                                goMiddle();
                            } else {
                                goNext();
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.d(TAG, "ScrollView ACTION_POINTER_UP");
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.d(TAG, "ScrollView ACTION_POINTER_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newY = ev.getY();
                    Log.d("SCROLL_TAB", "ScrollView newY:" + newY);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.d(TAG, "ScrollView ACTION_CANCEL");
                    break;
            }
        }
    };

    public void goFirst() {
        mTCurrentScreenType = TCurrentScreenType.EFirstScreen;
        switchScrollView.setCurrentScreenType(mTCurrentScreenType);
        switchScrollView.post(new Runnable() {
            @Override
            public void run() {
                switchScrollView.smoothScrollTo(0, 0);
            }
        });
    }

    public void goNext() {
        mTCurrentScreenType = TCurrentScreenType.ENextScreen;
        switchScrollView.setCurrentScreenType(mTCurrentScreenType);
        switchScrollView.post(new Runnable() {
            @Override
            public void run() {
                switchScrollView.smoothScrollTo(0, mScreenHeight * 2);
            }
        });

    }

    public void goMiddle() {
        mTCurrentScreenType = TCurrentScreenType.EMainScreen;
        switchScrollView.setCurrentScreenType(mTCurrentScreenType);
        switchScrollView.post(new Runnable() {
            @Override
            public void run() {
                switchScrollView.smoothScrollTo(0, mScreenHeight);
            }
        });
    }


    public int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public void setFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }
}
