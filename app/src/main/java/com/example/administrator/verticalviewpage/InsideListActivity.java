package com.example.administrator.verticalviewpage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InsideListActivity extends AppCompatActivity {
    private int mScreenWidth;
    private int mScreenHeight;
    private View mWrap1;
    private View mWrap2;
    private View mWrap3;
    NameAdapter nameAdapter;
    RecyclerView recyclerView;
    SwitchScrollView switchScrollView;
    String TAG = "MainActivity";
    private int mAffectedDistance = 300;
    private TCurrentTouchAreaType mTCurrentTouchAreaType = TCurrentTouchAreaType.ENone;
    private TCurrentScreenType mTCurrentScreenType = TCurrentScreenType.EMainScreen;
    float mStartY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_list);
        View mRoot = getWindow().getDecorView();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels - getStatusBarHeight(this);
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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG,"Event:" + event.getAction());
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:{
                        switchScrollView.setIsScrollable(false);
                    }
                    break;
                    case MotionEvent.ACTION_UP:{
                        switchScrollView.setIsScrollable(true);
                    }
                    break;
                }
                return false;
            }
        });
        initData();
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

    private void initData() {
        NameAdapter nameAdapter = new NameAdapter();
        List<String> name = new ArrayList<>();
        int i = 0;
        while (i < 100) {
            name.add("user" + i);
            i++;
        }
        nameAdapter.setData(name);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(nameAdapter);
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
}
