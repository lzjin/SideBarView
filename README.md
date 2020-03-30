# SideBarView
Android 字母索引View,类似电话联系人分类 
 
####博客地址  
[博客讲解地址](https://blog.csdn.net/lin857/article/details/105193760)

###效果图如下:
<img src="https://raw.githubusercontent.com/lzjin/SideBarView/master/imgfile/gif.gif">

###主要功能
支持侧边栏字母大小设置
支持侧边栏字母选中、未选中颜色设置
支持屏幕中间高亮TextView的字体大小、颜色、背景设置

###API方法介绍
onSideBarScrollUpdateItem("A")  侧边栏字母滑动 --> item
OnItemScrollUpdateText("B")  item滑动 --> 侧边栏字母

###Jitpack
---
Step 1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
	 ...
	 maven { url 'https://jitpack.io' }
    }
}
```
##### Gradle:
Step 2. Add the dependency
```
dependencies {
    //androidX 版本使用下面的依赖
    implementation 'com.github.lzjin:sidebar:1.0'
}
```

#### 在布局文件中添加 SideBarLayout
```
<com.lzj.sidebar.SideBarLayout
            android:id="@+id/sideBarLayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:sidebarSelectTextColor="@color/hotpink"
            app:sidebarUnSelectTextColor="@color/colorPrimary"
            app:sidebarSelectTextSize="12sp"
            app:sidebarUnSelectTextSize="10sp"
            app:sidebarWordBackground="@drawable/sort_text_bg"
            app:sidebarWordTextColor="@color/darkred"
            app:sidebarWordTextSize="45sp">
```
#### 侧边栏滑动 --> item
```
 sideBarLayout.setSideBarLayout(new SideBarLayout.OnSideBarLayoutListener() {
            @Override
            public void onSideBarScrollUpdateItem(String word) {
                //根据自己业务实现
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getWord().equals(word)) {
                        recyclerView.smoothScrollToPosition(i);
                        break;
                    }
                }
            }
        });
```
####  item滑动 --> 侧边栏
```
sideBarLayout.OnItemScrollUpdateText(mList.get(firstItemPosition).getWord());
```
