[ ![](https://api.bintray.com/packages/zhanghang/maven/recycleradapter/images/download.svg) ](https://bintray.com/zhanghang/maven/recycleradapter/_latestVersion)[ ![](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-%E5%BC%A0%E8%88%AA-red.svg) ](http://www.jianshu.com/u/22a5d2ee8385)
```
compile 'lib.kalu.adapter:recycleradapter:<latest-version>'
```

 [戳我下载 ==>](https://pan.baidu.com/s/1nvAAvpf)

![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-00.png )

#### BaseCommonAdapter: 基类, 添加头视图, 添加尾视图, 添加空视图
#### BaseCommonMultAdapter: 分类型， 继承BaseCommonAdapter
#### BaseCommonSwipeDragAdapter: 侧滑, 拖拽， 继承BaseCommonAdapter
#### BaseCommonTabAdapter: 分组， 继承BaseCommonAdapter
#### BaseLoadAdapter: 加载更多， 继承BaseCommonAdapter
#### BaseLoadMultAdapter: 加载更多, 分类型， 继承BaseLoadAdapter
#### BaseLoadSwipeDragAdapter: 加载更多, 侧滑, 拖拽， 继承BaseLoadAdapter
#### BaseLoadTabAdapter：加载更多, 分组， 继承BaseLoadAdapter

#### ***********************************************************************************************************************************

#### 一. 点击事件(RecyclerHolder.class, item、item-child)：

![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-12.gif )

```
BaseCommonAdapter adapter = new BaseCommonAdapter<String, RecyclerHolder>(List数据集合 , item布局文件id) {

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

#### ***********************************************************************************************************************************

#### 二. 加载更多(BaseLoadAdapter.class)：

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
        # isOver，是否需要显示loading，之后自己判断逻辑部分, 服务器告诉没有数据了, 需要调用loadOverNotifyDataSetChanged(recycler)
        @Override
        protected void onLoad(RecyclerHolder holder, boolean isOver) {
        }
    };
    
# 设置adapter
RecyclerView.setAdapter(adapter);
```

#### ***********************************************************************************************************************************

#### 三. 分类型布局(BaseCommonMultAdapter.class || BaseLoadMultAdapter.class)：
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

#### ***********************************************************************************************************************************

![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-09.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-06.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-04.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-05.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-10.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-07.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-03.gif ) 
