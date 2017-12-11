package com.demo.adapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.demo.adapter.R.id.sticky_recycler
import lib.kalu.adapter.BaseCommonAdapter
import lib.kalu.adapter.decoration.FloatDecoration
import lib.kalu.adapter.decoration.SpaceDecoration
import lib.kalu.adapter.holder.RecyclerHolder
import lib.kalu.adapter.manager.CrashLinearLayoutManager
import java.util.*

/**
 * description: 加载更多
 * created by kalu on 2017/12/5 3:34
 */
class FloatActivity : AppCompatActivity() {

    private val mRecyclerView: RecyclerView? by lazy {
        findViewById(sticky_recycler) as RecyclerView
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

    private val mLoadAdapter: BaseCommonAdapter<String, RecyclerHolder>? by lazy {

        object : BaseCommonAdapter<String, RecyclerHolder>(mArrayList, R.layout.activity_float_item) {

            override fun onNext(holder: RecyclerHolder, model: String, position: Int) {
                holder.setText(R.id.sticky_text, "第 ==> " + position.toString() + " <==个孩子")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_float)

        for (i in 0..20) {
            mArrayList?.add(i.toString())
        }

        val inflate = LayoutInflater.from(applicationContext).inflate(R.layout.activity_float_menu, null)
        inflate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                Toast.makeText(applicationContext, "点击", Toast.LENGTH_LONG).show();
            }
        })

        val floatDecoration = FloatDecoration(inflate)

        mRecyclerView!!.addItemDecoration(floatDecoration)
        mRecyclerView!!.layoutManager = mLayoutManager
        mRecyclerView!!.adapter = mLoadAdapter
    }
}