package com.demo.adapter

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.demo.adapter.model.tab.StickyActivity2
import lib.kalu.adapter.BaseCommonAdapter
import lib.kalu.adapter.decoration.SpaceDecoration
import lib.kalu.adapter.holder.RecyclerHolder
import lib.kalu.adapter.manager.CrashGridLayoutManager
import lib.kalu.adapter.manager.CrashLinearLayoutManager
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mActivity: Array<Class<*>>? by lazy {

        arrayOf<Class<*>>(ClickActivity::class.java, LoadmoreActivity::class.java, HeadFootActivity::class.java,
                MulitActivity::class.java, EmptyActivity::class.java,
                FloatActivity::class.java,
                StickyActivity2::class.java, StickyActivity1::class.java,
                SectionActivity::class.java,
                DragSwipeActivity::class.java,
                TransActivity::class.java)
    }

    private val mTitle: Array<String>? by lazy {
        arrayOf("点击事件", "加载更多", "头部尾部",
                "多种布局", "空布局",
                "悬浮菜单",
                "TabMore", "Tab",
                "分组",
                "侧滑拖动",
                "分组折叠")
    }

    private val mRecyclerView: RecyclerView? by lazy {
        findViewById(R.id.main_recycler) as RecyclerView
    }

    private val mArrayList: ArrayList<String>? by lazy {
        ArrayList<String>()
    }

    private val mLayoutManager: CrashGridLayoutManager? by lazy {
        CrashGridLayoutManager(applicationContext, 2)
    }

    private val mSpaceDecoration: SpaceDecoration? by lazy {
        SpaceDecoration(10)
    }

    private val mLoadAdapter: BaseCommonAdapter<String> by lazy {

        object : BaseCommonAdapter<String>(mArrayList, R.layout.activity_main_item) {

            override fun onNext(holder: RecyclerHolder, model: String, position: Int) {

                holder!!.setText(R.id.main_text, model)
                holder!!.setOnClickListener(View.OnClickListener {

                    startActivity(Intent(applicationContext, mActivity!![position]))
                }, R.id.main_text)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0 until mTitle!!.size) {
            mArrayList?.add(mTitle!![i])
        }

        mRecyclerView!!.addItemDecoration(mSpaceDecoration)
        mRecyclerView!!.layoutManager = mLayoutManager
        mRecyclerView!!.adapter = mLoadAdapter
    }
}