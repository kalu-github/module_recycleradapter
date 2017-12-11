[ ![Download](https://api.bintray.com/packages/zhanghang/maven/recycleradapter/images/download.svg) ](https://bintray.com/zhanghang/maven/recycleradapter/_latestVersion)
```
java版本：
compile 'lib.kalu.adapter:recycleradapter:<latest-version>'

kotlin版本 - 还没有搞好：
compile 'lib.kalu.adapter:recycleradapter_kotlin:<latest-version>'
```

 [戳我下载 ==>](https://pan.baidu.com/s/1jH5G8su)

![image](https://github.com/153437803/RecyclerAdapter/blob/master/20171205045053.png ) 

![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-11.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-09.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-08.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-06.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-04.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-05.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-10.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-07.gif ) 
![image](https://github.com/153437803/RecyclerAdapter/blob/master/Screenrecorder-2017-12-05-03.gif ) 

```
加载更多(BaseLoadAdapter.class)：

1. 创建adapter
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
2. 设置adapter
RecyclerView.setAdapter(adapter);
```
