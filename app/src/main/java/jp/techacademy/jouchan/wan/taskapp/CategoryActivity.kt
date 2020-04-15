package jp.techacademy.jouchan.wan.taskapp

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.category_input.*
import android.os.Bundle
import android.view.View
import io.realm.Realm

const val EXTRA_CATEGORY ="jp.techacademy.jouchan.wan.taskapp.CATEGORY"

class CategoryActivity : AppCompatActivity() {
    private var mCategory: Category? = null

    private val mOnDoneClickListener = View.OnClickListener {parent, _, position, _ ->
        val category = parent.adapter.getItem(position) as Category
        addCategory()
        intent.putExtra(EXTRA_CATEGORY, category.categoryid)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        done_button2.setOnClickListener(mOnDoneClickListener)

        val intent = intent
        val categoryId = intent.getIntExtra(EXTRA_CATEGORY, -1)
        val realm = Realm.getDefaultInstance()
        mCategory = realm.where(Category::class.java).equalTo("categoryid", categoryId).findFirst()
        realm.close()

        if (mCategory != null){
            category_edit_text.setText(mCategory!!.category)
        }
    }

    private fun addCategory() {
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        if (mCategory == null) {
            mCategory = Category()

            val categoryRealmResults = realm.where(Category::class.java).findAll()

            val identifier: Int =
                if (categoryRealmResults.max("categoryid") != null) {
                    categoryRealmResults.max("categoryid")!!.toInt() + 1
                } else {
                    0
                }
            mCategory!!.categoryid = identifier
        }

        val category = category_edit_text.text.toString()

        mCategory!!.category = category

        realm.copyToRealmOrUpdate(mCategory!!)
        realm.commitTransaction()

        realm.close()
    }
}
