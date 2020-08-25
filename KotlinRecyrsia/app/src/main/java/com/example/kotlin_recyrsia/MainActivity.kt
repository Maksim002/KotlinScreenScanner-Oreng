package com.example.kotlin_recyrsia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var myAdapter = ParentAdapter()
    private var list: ArrayList<ParentModel> = arrayListOf()

    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        parent_text_add.setOnClickListener {
            list.add(ParentModel("", position ++))
            myAdapter.update(list)
            myAdapter.notifyItemRangeChanged(list.size, list.size)
        }

        parent_recycler_view.adapter = myAdapter
    }
}