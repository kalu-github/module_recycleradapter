package com.demo.adapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import lib.kalu.adapter.BaseCommonAdapter
import lib.kalu.adapter.decoration.SpaceDecoration
import lib.kalu.adapter.holder.RecyclerHolder
import lib.kalu.adapter.manager.CrashGridLayoutManager
import java.util.*

class ClickActivity : AppCompatActivity() {

    private val mRecyclerView: RecyclerView? by lazy {
        findViewById(R.id.click_recycler) as RecyclerView
    }

    private val mArrayList: ArrayList<String>? by lazy {
        ArrayList<String>()
    }

    private val mLayoutManager: CrashGridLayoutManager? by lazy {
        CrashGridLayoutManager(applicationContext, 1)
    }

    private val mSpaceDecoration: SpaceDecoration? by lazy {
        SpaceDecoration(10f)
    }

    private val mLoadAdapter: BaseCommonAdapter<String> by lazy {

        object : BaseCommonAdapter<String>(mArrayList, R.layout.activity_click_item) {

            override fun onNext(holder: RecyclerHolder, model: String, position: Int) {

                holder!!.setOnClickListener(View.OnClickListener {
                    Toast.makeText(applicationContext, "点击第 " + position + " 个item", Toast.LENGTH_SHORT).show()
                }, R.id.click_text1)

                holder!!.setOnClickListener(View.OnClickListener {
                    Toast.makeText(applicationContext, "点击第 " + position + " 个item - child", Toast.LENGTH_SHORT).show()
                }, R.id.click_text2)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_click)

        for (i in 0 until 6) {
            mArrayList?.add(i.toString())
        }

        mRecyclerView!!.addItemDecoration(mSpaceDecoration)
        mRecyclerView!!.layoutManager = mLayoutManager
        mRecyclerView!!.adapter = mLoadAdapter
    }
}