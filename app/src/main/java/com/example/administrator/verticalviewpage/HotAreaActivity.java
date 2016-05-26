package com.example.administrator.verticalviewpage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HotAreaActivity extends AppCompatActivity {
    private int mScreenWidth;
    private int mScreenHeight;
    private View mWrap1;
    private View mWrap2;
    private View mWrap3;
    NameAdapter nameAdapter;
    RecyclerView recyclerView;
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
        initData();
    }


    private void initData(){
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
