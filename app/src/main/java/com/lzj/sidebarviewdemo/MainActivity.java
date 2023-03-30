package com.lzj.sidebarviewdemo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.lzj.sidebar.SideBarLayout;
import com.lzj.sidebarviewdemo.adapter.SortAdapter;
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
 * 参考示例
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
    SortAdapter mSortAdaper;
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
        createTestData();
        //进行排序
        Collections.sort(mList, new SortComparator());
        mSortAdaper = new SortAdapter(R.layout.itemview_sort, mList);
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

                    sidebarView.onItemScrollUpdateSideBarText(mList.get(firstItemPosition).getWord());
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
        String keyWord = s.toString().trim();
        Log.i("test","------------key="+keyWord);
        if (!TextUtils.isEmpty(keyWord)) {
            List<SortBean> searchList=matcherSearch(keyWord, mList);
            if (searchList.size() > 0) {
                sidebarView.onItemScrollUpdateSideBarText(searchList.get(0).getWord());
            }
            mSortAdaper.setNewData(searchList);
        } else {
            sidebarView.onItemScrollUpdateSideBarText(mList.get(0).getWord());
            mSortAdaper.setNewData(mList);
        }
        mSortAdaper.notifyDataSetChanged();
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


    /**
     * 创建测试数据
     */
    private void createTestData(){
        //A
        mList.add(new SortBean("阿三"," 成都敬江渠"));
        mList.add(new SortBean("阿刘"," 成都敬江渠"));
        mList.add(new SortBean("阿九"," 成都敬江渠"));
        //B
        mList.add(new SortBean("宝宝"," 成都敬江渠"));
        mList.add(new SortBean("包打听"," 成都敬江渠"));
        mList.add(new SortBean("豹子费"," 成都敬江渠"));
        //C
        mList.add(new SortBean("陈菲菲"," 成都敬江渠"));
        mList.add(new SortBean("陈大菲"," 成都敬江渠"));
        mList.add(new SortBean("车臣"," 成都敬江渠"));
        mList.add(new SortBean("程师妹"," 成都敬江渠"));
        //D
        mList.add(new SortBean("戴氏龙"," 成都敬江渠"));
        mList.add(new SortBean("德飞侠"," 成都敬江渠"));
        mList.add(new SortBean("刁峰"," 成都敬江渠"));
        //E
        mList.add(new SortBean("饿了么"," 成都敬江渠"));
        mList.add(new SortBean("恶人谷"," 成都敬江渠"));
        mList.add(new SortBean("额度"," 成都敬江渠"));
        //F
        mList.add(new SortBean("冯雨晴"," 成都敬江渠"));
        mList.add(new SortBean("飞儿"," 成都敬江渠"));
        // G
        mList.add(new SortBean("郭沫"," 成都敬江渠"));
        mList.add(new SortBean("果实李"," 成都敬江渠"));
        // H
        mList.add(new SortBean("海神"," 成都敬江渠"));
        mList.add(new SortBean("韩信"," 成都敬江渠"));
        mList.add(new SortBean("汉朝"," 成都敬江渠"));
        // I
        mList.add(new SortBean("I Love You"," 成都敬江渠"));
        // J
        mList.add(new SortBean("精忠报国"," 成都敬江渠"));
        mList.add(new SortBean("积极"," 成都敬江渠"));
        // K
        mList.add(new SortBean("康有为"," 成都敬江渠"));
        mList.add(new SortBean("康师傅"," 成都敬江渠"));
        // L
        mList.add(new SortBean("李白"," 成都敬江渠"));
        mList.add(new SortBean("李太白"," 成都敬江渠"));
        mList.add(new SortBean("李世民"," 成都敬江渠"));
        mList.add(new SortBean("林落"," 成都敬江渠"));
        // M
        mList.add(new SortBean("米老鼠"," 成都敬江渠"));
        mList.add(new SortBean("明日"," 成都敬江渠"));
        // N
        mList.add(new SortBean("你好啊"," 成都敬江渠"));
        // O
        mList.add(new SortBean("哦"," 成都敬江渠"));
        // P
        mList.add(new SortBean("骗你的"," 成都敬江渠"));
        // Q
        mList.add(new SortBean("情不自禁"," 成都敬江渠"));
        // R
        mList.add(new SortBean("日子不错"," 成都敬江渠"));
        // S
        mList.add(new SortBean("胜龙"," 成都敬江渠"));
        mList.add(new SortBean("神经病"," 成都敬江渠"));
        // T
        mList.add(new SortBean("提示"," 成都敬江渠"));
        mList.add(new SortBean("腾讯"," 成都敬江渠"));
        // U
        mList.add(new SortBean("U YB"," 成都敬江渠"));
        // V
        mList.add(new SortBean("V字头"," 成都敬江渠"));
        // W
        mList.add(new SortBean("王老三"," 成都敬江渠"));
        mList.add(new SortBean("王东的"," 成都敬江渠"));
        // X
        mList.add(new SortBean("新人"," 成都敬江渠"));
        mList.add(new SortBean("洗漱"," 成都敬江渠"));
        // Y
        mList.add(new SortBean("阳光"," 成都敬江渠"));
        mList.add(new SortBean("杨家枪"," 成都敬江渠"));
        // Z
        mList.add(new SortBean("张三"," 成都敬江渠"));
        mList.add(new SortBean("张龙"," 成都敬江渠"));
        mList.add(new SortBean("张笑龙"," 成都敬江渠"));
        // #
    }
}
