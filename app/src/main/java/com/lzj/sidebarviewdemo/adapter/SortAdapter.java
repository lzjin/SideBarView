package com.lzj.sidebarviewdemo.adapter;

import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lzj.sidebarviewdemo.R;
import com.lzj.sidebarviewdemo.bean.SortBean;
import java.util.List;

/**
 * 参考示例
 */
public class SortAdapter extends BaseQuickAdapter<SortBean, BaseViewHolder> {
    private List<SortBean> mData;

    public SortAdapter(int layoutResId, List<SortBean> data) {
        super(layoutResId, data);
        mData=data;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, SortBean item) {
        //TODO 参考代码，请根据自身业务实现

        //第一个字母显示
        if (viewHolder.getLayoutPosition() == 0) {
            (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
        } else {
            //然后判断当前姓名的首字母和上一个首字母是否相同,如果相同字母导航条就隐藏,否则就显示
//            if(mData.get(viewHolder.getLayoutPosition()).getWordAscii()==mData.get(viewHolder.getLayoutPosition()-1).getWordAscii()){
//                (viewHolder.getView(R.id.tv_key)).setVisibility(View.GONE);
//            }else {
//                (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
//            }

            //首字母和上一个首字母是否相同,如果相同字母导航条就影藏,否则就显示
            int section = getSectionForPosition(viewHolder.getLayoutPosition());
            if (viewHolder.getLayoutPosition() == getPositionForSection(section)) {
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.VISIBLE);
            } else {
                (viewHolder.getView(R.id.tv_key)).setVisibility(View.GONE);
            }
        }
        viewHolder.setText(R.id.tv_key, item.getWord());
        viewHolder.setText(R.id.tv_name, item.getName());
        viewHolder.setText(R.id.tv_address, item.getAddress());
    }

    /**
     * 根据View的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return getData().get(position).getWord().charAt(0);
    }

    /**
     * 获取第一次出现该首字母的List所在的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getData().size(); i++) {
            String sortStr = getData().get(i).getWord();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
