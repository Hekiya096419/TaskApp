package jp.techacademy.jouchan.wan.taskapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CategoryAdapter(context: Context): BaseAdapter() {
    private val mLayoutInflater : LayoutInflater
    var categorylist = mutableListOf<Category>()

    init {
        this.mLayoutInflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: mLayoutInflater.inflate(android.R.layout.simple_expandable_list_item_2, null)

        val textView1 = view.findViewById<TextView>(android.R.id.text1)
        textView1.text = categorylist[position].category

        return  view
    }

    override fun getItem(position: Int): Any {
        return categorylist[position]
    }

    override fun getItemId(position: Int): Long {
        return categorylist[position].id.toLong()
    }

    override fun getCount(): Int {
        return categorylist.size
    }
}