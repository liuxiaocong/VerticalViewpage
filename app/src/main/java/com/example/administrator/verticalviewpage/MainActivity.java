package com.example.administrator.verticalviewpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by LiuXiaocong on 5/26/2016.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.go_inside_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, InsideListActivity.class);
                startActivity(it);
            }
        });
        findViewById(R.id.go_full_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, ToggleFullscreenActivity.class);
                startActivity(it);
            }
        });
        findViewById(R.id.go_hot_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, HotAreaActivity.class);
                startActivity(it);
            }
        });
    }
}
