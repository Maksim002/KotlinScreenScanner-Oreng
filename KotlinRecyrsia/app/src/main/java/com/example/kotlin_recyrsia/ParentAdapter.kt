package com.example.kotlin_recyrsia

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_parent.view.*


class ParentAdapter(val date: ArrayList<ParentModel> = arrayListOf()) :
    GenericRecyclerAdapter<ParentModel>(date) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_parent)
    }

    override fun bind(item: ParentModel, holder: ViewHolder) {
        val myAdapter = ParentAdapter()
        myAdapter.items = item.list
        holder.itemView.parent_recycler.adapter = myAdapter
        holder.itemView.parent_text.setText(item.title)
        holder.itemView.parent_add.setOnClickListener {
            item.list.add(ParentModel("", items.size))
            notifyItemRangeChanged(holder.adapterPosition, item.list.size)
        }

        holder.itemView.parent_clear.setOnClickListener {
            items.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            notifyItemRangeChanged(holder.adapterPosition, date.size)
        }


        holder.itemView.parent_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    item.title = holder.itemView.parent_text.text.toString()
                } catch (e: Exception) {
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}