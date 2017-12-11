[ ![Download](https://api.bintray.com/packages/zhanghang/maven/recycleradapter/images/download.svg) ](https://bintray.com/zhanghang/maven/recycleradapter/_latestVersion)
```
java版本：
compile 'lib.kalu.adapter:recycleradapter:<latest-version>'

kotlin版本 - 还没有搞好：
compile 'lib.kalu.adapter:recycleradapter_kotlin:<latest-version>'
```

 [戳我下载 ==>](https://pan.baidu.com/s/1jH5G8su)

![image](https://github.com/153437803/RecyclerAdapter/blob/master/20171205045053.png ) 

<font color=red size=5>加载更多(BaseLoadAdapter.class)：</font>
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-11.gif ) 
```
# 加载结束, 没有数据了
loadOverNotifyDataSetChanged(RecyclerView recycler);

# 加载完成, 不显示loading
loadCompleteNotifyDataSetChanged(RecyclerView recycler);

# 刷新数据, 重置所有标记位
loadResetNotifyDataSetChanged(RecyclerView recycler);

# 创建adapter
BaseLoadAdapter adapter = new BaseLoadAdapter<String, RecyclerHolder>(List数据集合 , item布局文件id, load布局文件id) {

        # 设置数据
        @Override
        protected void onNext(RecyclerHolder holder, String model, int position) {
        }

        # 加载监听
        @Override
        protected void onLoad() {
         }
    };
    
# 设置adapter
RecyclerView.setAdapter(adapter);
```

#### 分类型布局(BaseCommonMultAdapter.class || BaseLoadMultAdapter.class)：
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
BaseCommonMultAdapter adapter = new BaseCommonMultAdapter<String, RecyclerHolder>(List数据集合) {

        # 合并单元格
        @Override
        protected void onMerge() {
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

![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-09.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-06.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-04.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-05.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-10.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-07.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-03.gif ) 
