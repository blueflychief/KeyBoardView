package com.infinite.keyboardapp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Author： lsq
 * Time： 7/17/2018 - 08:50 PM
 * Describe：类似于微信的聊天键盘界面
 * 必须在AndroidManifest文件中设置Activity的android:windowSoftInputMode="adjustNothing"
 */
public class KeyboardView extends LinearLayout implements View.OnClickListener {
    private static int sKeyboardHeight;

    private FrameLayout flExtendPanel;
    private FrameLayout flContentPanel;
    private Button btPic;
    private EditText etInput;
    private View viewMask;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        int layout = R.layout.merge_key_board_view;
        flExtendPanel = findViewById(R.id.flExtendPanel);
        flContentPanel = findViewById(R.id.flContentPanel);
        btPic = findViewById(R.id.btPic);
        etInput = findViewById(R.id.etInput);
        viewMask = findViewById(R.id.viewMask);
        btPic.setTag(false);
        btPic.setOnClickListener(this);
        viewMask.setOnClickListener(this);
        flContentPanel.setOnClickListener(this);
    }

    /**
     * 设置内容区域
     *
     * @param view
     */
    public void setContentView(View view) {
        flContentPanel.addView(view);
    }

    /**
     * 设置扩展区域的内容
     *
     * @param view
     */
    public void setExtendView(View view) {
        flExtendPanel.addView(view);
    }

    /**
     * 获取键盘的高度，这个值只有在使用过{@link KeyboardPopWindow}后才有效
     *
     * @return
     */
    public static int getKeyboardHeight() {
        return sKeyboardHeight;
    }

    public void showExtendPanel(int height) {
        if (sKeyboardHeight == 0 && height > 0) {
            sKeyboardHeight = height;
        }
        boolean showExtend = (boolean) btPic.getTag();
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.height = showExtend ? sKeyboardHeight : height;
        flExtendPanel.setLayoutParams(layoutParams);
        viewMask.setVisibility(showExtend || height > 0 ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btPic:
                btPic.setTag(true);
                hiddenKeyBoard();
                showExtendPanel(sKeyboardHeight);
                break;
            case R.id.viewMask:
                hiddenKeyBoard();
                btPic.setTag(false);
                showExtendPanel(0);
                break;
            default:
                break;
        }
    }

    /**
     * hide keyboard
     */
    private void hiddenKeyBoard() {
        InputMethodManager imm = (InputMethodManager) etInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
        }

    }

    /**
     * popup keyboard
     */
    private void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) etInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(etInput, InputMethodManager.SHOW_FORCED);
        }
    }
}
