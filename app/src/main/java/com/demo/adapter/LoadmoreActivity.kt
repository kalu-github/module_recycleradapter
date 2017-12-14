package com.demo.adapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.demo.adapter.R.id.loadmore_recycler
import lib.kalu.adapter.BaseLoadAdapter
import lib.kalu.adapter.decoration.SpaceDecoration
import lib.kalu.adapter.holder.RecyclerHolder
import lib.kalu.adapter.manager.CrashLinearLayoutManager
import java.util.*

/**
 * description: 加载更多
 * created by kalu on 2017/12/5 3:34
 */
class LoadmoreActivity : AppCompatActivity() {

    private val mRecyclerView: RecyclerView? by lazy {
        findViewById(loadmore_recycler) as RecyclerView
    }

    private val mArrayList: ArrayList<String>? by lazy {
        ArrayList<String>()
    }

    private val mLayoutManager: CrashLinearLayoutManager? by lazy {
        CrashLinearLayoutManager(applicationContext)
    }

    private val mSpaceDecoration: SpaceDecoration? by lazy {
        SpaceDecoration(10)
    }

    private val mLoadAdapter: BaseLoadAdapter<String> by lazy {

        object : BaseLoadAdapter<String>(mArrayList, R.layout.activity_loadmore_item, R.layout.activity_loadmore_loading) {
            override fun onLoad(holder: RecyclerHolder?, isOver: Boolean) {
                if (isOver) {

                } else {
                    Thread(Runnable {

                        Thread.sleep(2000)
                        runOnUiThread({
                            for (i in 0..2) {
                                mArrayList?.add(i.toString())
                            }
                            loadOverNotifyDataSetChanged(mRecyclerView)
                            holder!!.setText(R.id.loading_text, "-- 我也是有底线的 --")
                            holder!!.setVisible(R.id.loading_cycle, View.GONE)
                        })
                    }).start()
                }
            }

            override fun onNext(holder: RecyclerHolder, model: String, position: Int) {
                holder.setText(R.id.loadmore_text, "第 ==> " + position.toString() + " <==个孩子")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadmore)

        for (i in 0..20) {
            mArrayList?.add(i.toString())
        }

        mRecyclerView!!.addItemDecoration(mSpaceDecoration)
        mRecyclerView!!.layoutManager = mLayoutManager
        mRecyclerView!!.adapter = mLoadAdapter
    }
}
