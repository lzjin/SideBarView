package com.lzj.sidebarviewdemo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.lzj.sidebar.SideBarLayout;
import com.lzj.sidebar.SideBarSortView;
import com.lzj.sidebarviewdemo.adapter.SortAdaper;
import com.lzj.sidebarviewdemo.bean.SortBean;
import com.lzj.sidebarviewdemo.utils.SortComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 使用简单示例
 * 推荐使用listview结合联动
 */
public class MainActivity extends AppCompatActivity  implements   TextWatcher {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sidebar)
    SideBarLayout sidebarView;
    SortAdaper mSortAdaper;
    List<SortBean> mList;
    private int mScrollState = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImmersionBar.with(this).transparentStatusBar().fitsSystemWindows(false).statusBarDarkFont(false).init();
        edtSearch.addTextChangedListener(this);
        mScrollState = -1;
        initData();
        connectData();
    }
    private void initData() {
        mList = new ArrayList<>();
        mList.add(new SortBean("阿三"," 成都敬江渠"));
        mList.add(new SortBean("宝宝"," 成都敬江渠"));
        mList.add(new SortBean("张三"," 成都敬江渠"));
        mList.add(new SortBean("张龙"," 成都敬江渠"));
        mList.add(new SortBean("张笑龙"," 成都敬江渠"));
        mList.add(new SortBean("李世民"," 成都敬江渠"));
        mList.add(new SortBean("冯雨晴"," 成都敬江渠"));
        mList.add(new SortBean("陈菲菲"," 成都敬江渠"));
        mList.add(new SortBean("康有为"," 成都敬江渠"));
        mList.add(new SortBean("康师傅"," 成都敬江渠"));
        mList.add(new SortBean("刊史龙"," 成都敬江渠"));
        mList.add(new SortBean("胜龙"," 成都敬江渠"));
        mList.add(new SortBean("神经病"," 成都敬江渠"));
        mList.add(new SortBean("飞儿"," 成都敬江渠"));
        mList.add(new SortBean("林落"," 成都敬江渠"));
        mList.add(new SortBean("陈迂曲"," 成都敬江渠"));
        mList.add(new SortBean("李白"," 成都敬江渠"));
        mList.add(new SortBean("李太白"," 成都敬江渠"));
        //进行排序
        Collections.sort(mList, new SortComparator());
        mSortAdaper = new SortAdaper(R.layout.itemview_sort, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //设置LayoutManager为LinearLayoutManager
        recyclerView.setAdapter(mSortAdaper);
        recyclerView.setNestedScrollingEnabled(false);//解决滑动不流畅

    }
    private void connectData() {
        //侧边栏滑动 --> item
        sidebarView.setSideBarLayout(new SideBarLayout.OnSideBarLayoutListener() {
            @Override
            public void onSideBarScrollUpdateItem(String word) {
                //循环判断点击的拼音导航栏和集合中姓名的首字母,如果相同recyclerView就跳转指定位置
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getWord().equals(word)) {
                        recyclerView.smoothScrollToPosition(i);
                        break;
                    }
                }
            }
        });
        //item滑动 --> 侧边栏
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int scrollState) {
                super.onScrollStateChanged(recyclerView, scrollState);
                mScrollState = scrollState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mScrollState != -1) {
                    //第一个可见的位置
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int firstItemPosition=0;
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        //获取第一个可见view的位置
                        firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    }

                    sidebarView.OnItemScrollUpdateText(mList.get(firstItemPosition).getWord());
                    if (mScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        mScrollState = -1;
                    }
                }
            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mList == null || mList.size() <= 0) {
            return;
        }
        String keyWord = s.toString();
        Log.i("test","------------key="+keyWord);
        if (!keyWord.equals("")) {
            if (matcherSearch(keyWord, mList).size() > 0) {
                sidebarView.OnItemScrollUpdateText(matcherSearch(keyWord, mList).get(0).getWord());
            }
            mSortAdaper.setNewData(matcherSearch(keyWord, mList));
            mSortAdaper.notifyDataSetChanged();
        } else {
            sidebarView.OnItemScrollUpdateText(mList.get(0).getWord());
            mSortAdaper.setNewData(mList);
            mSortAdaper.notifyDataSetChanged();
        }
    }

    /**
     * 匹配输入数据
     *
     * @param keyword
     * @param list
     * @return
     */
    public List<SortBean> matcherSearch(String keyword, List<SortBean> list) {
        List<SortBean> results = new ArrayList<>();
        String patten = Pattern.quote(keyword);
        Pattern pattern = Pattern.compile(patten, Pattern.CASE_INSENSITIVE);
        for (int i = 0; i < list.size(); i++) {
            //根据首字母
            Matcher matcherWord = pattern.matcher((list.get(i)).getWord());
            //根据拼音
            Matcher matcherPin = pattern.matcher((list.get(i)).getPinyin());
            //根据简拼
            Matcher matcherJianPin = pattern.matcher((list.get(i)).getJianpin());
            //根据名字
            Matcher matcherName = pattern.matcher((list.get(i)).getName());
            if (matcherWord.find() || matcherPin.find() || matcherName.find() || matcherJianPin.find()) {
                results.add(list.get(i));
            }
        }

        return results;
    }
}
