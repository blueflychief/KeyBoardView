package com.infinite.keyboardapp;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Author： lsq
 * Time： 7/17/2018 - 09:21 PM
 * Describe：用于检测软键盘的高度，只对竖屏有效
 * 必须在AndroidManifest文件中设置Activity的android:windowSoftInputMode="adjustNothing"
 * 必须在Activity关闭的时候手动调用{@link #dismiss()}方法
 */
public class KeyboardPopWindow extends PopupWindow {
    private Activity activity;
    private OnKeyboardHeightChangeListener heightChangeListener;

    public KeyboardPopWindow(Activity activity) {
        super(activity);
        this.activity = activity;
        setContentView(new View(activity));
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        update();
        setWidth(0);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        getContentView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(globalLayoutListener);
    }

    public KeyboardPopWindow show(View parent) {
        showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
        return this;
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            handleOnGlobalLayout();
        }
    };

    private void handleOnGlobalLayout() {
        Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        Rect rect = new Rect();
        getContentView().getWindowVisibleDisplayFrame(rect);
        int keyboardHeight = screenSize.y - rect.bottom;
        if (heightChangeListener != null) {
            heightChangeListener.onKeyboardHeightChange(keyboardHeight, keyboardHeight > 100);
        }
    }

    public KeyboardPopWindow setKeyboardHeightChangeListener(OnKeyboardHeightChangeListener heightChangeListener) {
        this.heightChangeListener = heightChangeListener;
        return this;
    }

    public interface OnKeyboardHeightChangeListener {
        void onKeyboardHeightChange(int height, boolean isShow);
    }

    @Override
    public void dismiss() {
        getContentView()
                .getViewTreeObserver()
                .removeOnGlobalLayoutListener(globalLayoutListener);
        super.dismiss();
    }
}
