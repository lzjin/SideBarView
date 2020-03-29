package com.lzj.sidebarviewdemo.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lzj.sidebarviewdemo.R;
import com.lzj.sidebarviewdemo.bean.SortBean;

import java.util.List;

public class SortAdaper extends BaseQuickAdapter<SortBean, BaseViewHolder> {
    List<SortBean> mData;
    public SortAdaper(int layoutResId, List<SortBean> data) {
        super(layoutResId, data);
        mData=data;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, SortBean item) {

        //第一个字母显示
        if (viewHolder.getLayoutPosition() == 0) {
            (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
        } else {
            //然后判断当前姓名的首字母和上一个首字母是否相同,如果相同字母导航条就影藏,否则就显示
            if(mData.get(viewHolder.getLayoutPosition()).getWord().equals(mData.get(viewHolder.getLayoutPosition()-1).getWord())){
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.GONE);
            }else {
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
            }
        }
        viewHolder.setText(R.id.tv_key, item.getWord());
        viewHolder.setText(R.id.tv_name, item.getName());
        viewHolder.setText(R.id.tv_address, item.getAddress());
    }


}
