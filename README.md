[ ![](https://api.bintray.com/packages/zhanghang/maven/recycleradapter/images/download.svg) ](https://bintray.com/zhanghang/maven/recycleradapter/_latestVersion) ![](https://img.shields.io/badge/Build-Passing-green.svg) ![](https://img.shields.io/badge/API%20-14+-green.svg) [ ![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E5%BC%A0%E8%88%AA-red.svg) ](http://www.jianshu.com/u/22a5d2ee8385) ![](https://img.shields.io/badge/%E9%82%AE%E7%AE%B1-153437803@qq.com-red.svg)
```
compile 'lib.kalu.adapter:recycleradapter:<latest-version>'
```

 [戳我下载 ==>](https://pan.baidu.com/s/1nvAAvpf)

![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-00.png )

#

# 1.点击事件(RecyclerHolder.class, item、item-child)：

![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-12.gif )

```
BaseCommonAdapter adapter = new BaseCommonAdapter<String>(List数据集合 , item布局文件id) {

        # 设置数据
        @Override
        protected void onNext(RecyclerHolder holder, String model, int position) {
        
                holder!!.setOnClickListener(View.OnClickListener {
                    Toast.makeText(applicationContext, "点击第 " + position + " 个item", Toast.LENGTH_SHORT).show()
                }, R.id.click_text1)

                holder!!.setOnClickListener(View.OnClickListener {
                    Toast.makeText(applicationContext, "点击第 " + position + " 个item - child", Toast.LENGTH_SHORT).show()
                }, R.id.click_text2)
        }
    };
```

#

# 2.加载更多(BaseLoadAdapter.class)：

![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-11.gif ) 
```
# 加载结束, 没有数据了
loadOverNotifyDataSetChanged(RecyclerView recycler);

# 加载完成, 不显示loading
loadCompleteNotifyDataSetChanged(RecyclerView recycler);

# 刷新数据, 重置所有标记位
loadResetNotifyDataSetChanged(RecyclerView recycler);

# 创建adapter
BaseLoadAdapter adapter = new BaseLoadAdapter<String>(List数据集合 , item布局文件id, load布局文件id) {

        # 设置数据
        @Override
        protected void onNext(RecyclerHolder holder, String model, int position) {
        }

        # 加载监听
        # isOver，是否需要显示loading，之后自己判断逻辑部分, 服务器告诉没有数据了, 需要调用loadOverNotifyDataSetChanged(recycler)
        @Override
        protected void onLoad(RecyclerHolder holder, boolean isOver) {
        }
    };
    
# 设置adapter
RecyclerView.setAdapter(adapter);
```

#

# 3.分类型布局(BaseCommonMultAdapter.class || BaseLoadMultAdapter.class)：
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-08.gif ) 
```
# 创建bean, 实现MultModel
class NewMulitItem implements MultModel {

    // 布局类型
    类型private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}

# 创建adapter
BaseCommonMultAdapter adapter = new BaseCommonMultAdapter<String>(List数据集合) {

        # 合并单元格
        @Override
        protected int onMerge(int position) {
        }
         
        # 添加分类型布局
        @Override
        protected void onMult() {       
            addItemType(布局类型(int), item布局文件id);
        }

        # 设置数据
        @Override
        protected void onNext(RecyclerHolder holder, String model, int position) {
        }
    };
    
# 设置adapter
RecyclerView.setAdapter(adapter);
```
#

# 4.添加头添加尾（BaseCommonAdapter.class）：
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-09.gif ) 
```
# 添加头
BaseCommonMultAdapter.addHeadView(headView)

# 添加尾
BaseCommonMultAdapter.addFootView(footView)
    
# 设置adapter
RecyclerView.setAdapter(adapter);
```
#

# 5.空，加载，错误布局（BaseCommonAdapter.class）：
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-10.gif ) 
```
#  空布局
BaseCommonMultAdapter.setNullView(nullView);
    
# 设置adapter
RecyclerView.setAdapter(adapter);
```

#

# 6.分组（BaseCommonSectionAdapter.class || BaseLoadSectionAdapter.class）：
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-06.gif ) 
```
# 创建bean, 实现SectionModel
class MySection extends SectionModel<Video> {
    private boolean isMore;

    public MySection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public MySection(Video t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}

# 创建adapter
BaseCommonSectionAdapter adapter = new BaseCommonSectionAdapter<MySection>(List数据集合, item布局id, section布局id) {

        # 分组
        @Override
        protected void onSection(RecyclerHolder holder, int position) {
        }
         
        # 设置数据
        @Override
        protected void onNext(RecyclerHolder holder, MySection model, int position) {
        }
    };
    
# 设置adapter
RecyclerView.setAdapter(adapter);
```

#

# 6.多级菜单（BaseCommonMultAdapter.class || BaseLoadMultAdapter.class）：
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-05.gif )
```
# 创建bean, 实现MultModel, 继承
# 一级菜单
class Level0Item extends TransModel<Level1Item> implements MultModel {
    public String title;
    public String subTitle;

    public Level0Item(String title, String subTitle) {
        this.subTitle = subTitle;
        this.title = title;
    }

    @Override
    public int getItemType() {
        return TransAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
# 二级菜单
class Level1Item extends TransModel<Person> implements MultModel {
    public String title;
    public String subTitle;

    public Level1Item(String title, String subTitle) {
        this.subTitle = subTitle;
        this.title = title;
    }

    @Override
    public int getItemType() {
        return TransAdapter.TYPE_LEVEL_1;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
# 三级菜单
class Person implements MultModel {
    public Person(String name, int age) {
        this.age = age;
        this.name = name;
    }

    public String name;
    public int age;

    @Override
    public int getItemType() {
        return TransAdapter.TYPE_PERSON;
    }
}

# 创建adapter
BaseCommonMultAdapter adapter = new BaseCommonMultAdapter<MySection>(List数据集合) {

         # 添加分类型布局
        @Override
        protected void onMult() {       
            addItemType(一级菜单布局类型(int), 一级菜单item布局文件id);
            addItemType(二级菜单布局类型(int), 二级菜单item布局文件id);
            addItemType(三级菜单布局类型(int), 三级菜单item布局文件id);
        }
         
        # 设置数据
        @Override
        protected void onNext(RecyclerHolder holder, MySection model, int position) {
            switch (holder.getItemViewType()) {
              case: 一级菜单布局类型(int):
                  break;
              case: 二级菜单布局类型(int):
                  break;
              case: 三级菜单布局类型(int):
                  break;
            ｝
        }
    };
    
# 设置adapter
RecyclerView.setAdapter(adapter);
```
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-07.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-03.gif )

#

# Proguard-Rules
```
-keep class lib.kalu.adapter.** {
*;
}
-keep public class * extends lib.kalu.adapter.BaseCommonAdapter
```

#

# License
```
Copyright 2016 张航

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
