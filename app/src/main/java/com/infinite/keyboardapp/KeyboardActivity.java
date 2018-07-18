package com.infinite.keyboardapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class KeyboardActivity extends AppCompatActivity {
    private static final String TAG = KeyboardActivity.class.getSimpleName();

    private KeyboardView keyboardView;
    private ListView lvList;
    private String[] city = {
            "广州", "深圳", "北京", "上海", "香港",
            "澳门", "天津", "武汉", "长沙", "昆山",
            "三亚", "海口", "昆明", "南京", "重庆",
            "成都", "杭州", "南昌", "桂林", "沈阳"};
    private KeyboardPopWindow popWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        keyboardView = findViewById(R.id.keyBoardView);
        lvList = new ListView(this);
        keyboardView.setContentView(lvList);
        lvList.setAdapter(new ArrayAdapter<String>(KeyboardActivity.this, android.R.layout.simple_list_item_1, city));
        keyboardView.post(new Runnable() {
            @Override
            public void run() {
                popWindow = new KeyboardPopWindow(KeyboardActivity.this)
                        .show(lvList)
                        .setKeyboardHeightChangeListener(new KeyboardPopWindow.OnKeyboardHeightChangeListener() {
                            @Override
                            public void onKeyboardHeightChange(int height, boolean isShow) {
                                Log.i(TAG, "onKeyboardHeightChange height:" + height + " isShow:" + isShow);
                                keyboardView.showExtendPanel(height);
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popWindow != null && popWindow.isShowing()) {
            popWindow.dismiss();
        }
    }
}
