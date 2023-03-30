package com.lzj.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.Nullable;

/**
 * Created by lzj on 2019/12/31
 * Describe ：字母排序索引
 */
public class SideBarSortView extends View {
    private Canvas mCanvas;
    private int mSelectIndex = 0;
    private float mTextSize;
    private int mTextColor;
    private float mTextSizeChoose;
    private int mTextColorChoose;
    //标记 避免重复调用
    private boolean isDown = false;

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public void setmTextSizeChoose(float mTextSizeChoose) {
        this.mTextSizeChoose = mTextSizeChoose;
    }

    public void setmTextColorChoose(int mTextColorChoose) {
        this.mTextColorChoose = mTextColorChoose;
    }

    public static String[] mList = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    public Paint paint = new Paint();

    public SideBarSortView(Context context) {
        super(context);
    }

    public SideBarSortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        paintText();
    }

    private void paintText() {
        //计算每一个字母的高度,总告诉除以字母集合的高度就可以
        int height = (getHeight()) / mList.length;
        for (int i = 0; i < mList.length; i++) {
            if (i == mSelectIndex) {
                paint.setColor(mTextColorChoose);
                paint.setTextSize(mTextSizeChoose);
            } else {
                paint.setColor(mTextColor);
                paint.setTextSize(mTextSize);
            }
            paint.setAntiAlias(true);//设置抗锯齿
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            //计算每一个字母x轴
            float paintX = getWidth() / 2F - paint.measureText(mList[i]) / 2;
            //计算每一个字母Y轴
            int paintY = height * i + height;
            //绘画出来这个TextView
            mCanvas.drawText(mList[i], paintX, paintY, paint);
            //画完一个以后重置画笔
            paint.reset();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewParent parent;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int index = (int) (event.getY() / getHeight() * mList.length);
                if (index >= 0 && index < mList.length && mSelectIndex != index) {
                    if (mClickListener != null) {
                        mClickListener.onSideBarScrollUpdateItem(mList[index]);
                    }
                    mSelectIndex = index;
                    invalidate();
                    //改变标记状态
                    isDown = true;
                }
                parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);// 请求父级拦截，解决水平滑动冲突
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mClickListener != null) {
                    mClickListener.onSideBarScrollEndHideText();
                }
                //改变标记状态
                isDown = false;

                parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);// 请求父级放行，解决水平滑动冲突
                }
                break;
        }
        return true;
    }

    private OnIndexChangedListener mClickListener;

    public static interface OnIndexChangedListener {
        //滚动位置
        void onSideBarScrollUpdateItem(String word);

        //隐藏提示文本
        void onSideBarScrollEndHideText();
    }

    public void setIndexChangedListener(OnIndexChangedListener listener) {
        this.mClickListener = listener;
    }

    /**
     * Item滚动 更新侧边栏字母
     *
     * @param word 字母
     */
    public void onUpdateSideBarText(String word) {
        //手指没触摸才调用
        if (!isDown) {
            for (int i = 0; i < mList.length; i++) {
                if (mList[i].equals(word) && mSelectIndex != i) {
                    mSelectIndex = i;
                    invalidate();
                }
            }
        }
    }

}

