package com.lzj.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

/**
 * Created by lzj on 2019/12/31
 * Describe ：字母排序索引
 */
public class SideBarSortView extends View {
    private Context mContext;
    private Canvas mCanvas;
    private int mChoose = 0;
    public static String[] mList = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z","#"};
    public Paint paint = new Paint();
    public SideBarSortView(Context context) {
        super(context);
        this.mContext=context;
    }
    public SideBarSortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas=canvas;
        paintText();
    }

    private void paintText(){
        //计算每一个字母的高度,总告诉除以字母集合的高度就可以
        int height = (getHeight()) / mList.length;
        for (int i = 0; i < mList.length; i++) {
            if(i==mChoose){
                paint.setColor(Color.parseColor("#2E56D7"));
            }else {
                paint.setColor(Color.parseColor("#208DFA"));
            }
            paint.setAntiAlias(true);//设置抗锯齿
            paint.setTextSize( Utils.dip2px(mContext,11));
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int index = (int) (event.getY() / getHeight() * mList.length);
                if (index >= 0 && index < mList.length) {
                    if(mClickListener!=null){
                        mClickListener.onIndexChanged(mList[index]);
                    }
                    mChoose=index;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if(mClickListener!=null){
                    mClickListener.onIndexCancelHide();
                }
                break;
        }
        return true;
    }

    //1、滑动点击联动Item或隐藏提醒
    private OnIndexChangedListener mClickListener;

    public static interface OnIndexChangedListener {
        void onIndexChanged(String word);
        void onIndexCancelHide();
    }

    public void setIndexChangedListener(OnIndexChangedListener listener) {
        this.mClickListener = listener;
    }
    //2、设置滚动联动  字母
    public void setScrollIndex(String word) {
        for (int i = 0; i < mList.length; i++) {
            if(mList[i].equals(word)&& mChoose != i ){
                this.mChoose = i;
                invalidate();
            }
        }
    }

}

